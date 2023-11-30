package io.irw.hawk.scraper.service.scrape;

import static io.irw.hawk.scraper.model.ProcessingPipelineStep.linearExecutionGraphByDependencies;

import com.ebay.buy.browse.api.ItemSummaryApi;
import com.ebay.buy.browse.model.ItemSummary;
import com.ebay.buy.browse.model.SearchPagedCollection;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.dto.ebay.SearchTermDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.integration.ebay.buy.browse.ItemSummaryApiWrapper;
import io.irw.hawk.mapper.EbayFindingMapper;
import io.irw.hawk.mapper.EbaySellerMapper;
import io.irw.hawk.scraper.exceptions.ScrapingException;
import io.irw.hawk.scraper.model.MerchandiseReasoningLog;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ProcessingPipelineMetadata;
import io.irw.hawk.scraper.service.domain.EbayFindingService;
import io.irw.hawk.scraper.service.domain.EbayHighlightService;
import io.irw.hawk.scraper.service.domain.EbaySellerService;
import io.irw.hawk.scraper.service.domain.HawkScrapeRunService;
import io.irw.hawk.scraper.service.extractors.ItemSummaryDataExtractor;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import io.irw.hawk.scraper.service.processors.ProductScrapeProcessor;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class ScraperService {

  ProcessingPipelineStep[] processingPipelineSteps;

  ItemSummaryApiWrapper itemSummaryApi;
  ItemSummaryApi itemApi;

  ScrapeTargetProviderService scrapeTargetProviderService;
  HawkScrapeRunService hawkScrapeRunService;
  List<ProductScrapeProcessor> scrapeProcessors;

  EbaySellerMapper ebaySellerMapper;
  EbayFindingMapper ebayFindingMapper;

  EbaySellerService ebaySellerService;
  EbayFindingService ebayFindingService;
  EbayHighlightService ebayHighlightService;

  private AtomicBoolean isScraping = new AtomicBoolean(false);

  public void scrape() {
    isScraping.set(true);
    while (isScraping.get()) {
      scrapeTargetProviderService.getNextScrapeTarget()
          .ifPresentOrElse(this::scrapePV, () -> isScraping.set(false));
    }
  }

  private void scrapePV(ProductVariantEnum targetProductVariant) {
    HawkScrapeRunDto hawkScrapeRunDto = hawkScrapeRunService.startScrapeRun(targetProductVariant);

    ProductScrapeProcessor productScrapeProcessor = findMatchingScrapingProcessor(targetProductVariant);
    for (SearchTermDto searchTermDto : productScrapeProcessor.generateSearchTerms(targetProductVariant)) {
      SearchPagedCollection result = itemSummaryApi.search(searchTermDto.getSearchParams());

      if (result.getItemSummaries() == null) {
        log.warn("No items found for search term: {}", searchTermDto);
        continue;
      }

      for (ItemSummary itemSummary : result.getItemSummaries()) {
        processItemSummary(targetProductVariant, itemSummary, hawkScrapeRunDto);
      }
    }
  }

  private void processItemSummary(ProductVariantEnum targetProductVariant, ItemSummary itemSummary,
      HawkScrapeRunDto hawkScrapeRunDto) {
    EbaySellerDto ebaySellerDto = ebaySellerService.upsertSeller(
        ebaySellerMapper.sellerToEbaySellerDto(itemSummary.getSeller()));

    EbayHighlightDto highlightDto = EbayHighlightDto.builder()
        .run(hawkScrapeRunDto)
        .ebayFinding(getEbayFindingDto(itemSummary, ebaySellerDto))
        .pipelineMetadata(new ProcessingPipelineMetadata())
        .aggregatedVerdict(determineInitialVerdictBasedOnListingType(itemSummary))
        .build();

    for (ProcessingPipelineStep pipelineStep : linearExecutionGraphByDependencies(processingPipelineSteps)) {
      highlightDto.getPipelineMetadata()
          .newStep(pipelineStep.getClass());
      if (pipelineStep instanceof ItemSummaryMatcher itemSummaryMatcher) {
        applyMatcher(targetProductVariant, itemSummary, itemSummaryMatcher, highlightDto);
        if (highlightDto.getAggregatedVerdict() == MerchandiseVerdictType.ITEM_ALREADY_PERSISTED) {
          break;
        }
      } else if (pipelineStep instanceof ItemSummaryDataExtractor itemSummaryDataExtractor) {
        applyDataExtractor(itemSummary, itemSummaryDataExtractor, highlightDto);
      } else {
        throw new IllegalStateException("Unknown pipeline step: " + pipelineStep);
      }
    }

    EbayFindingDto persistedEbayFindingDto = saveOrUpdateEbayFinding(highlightDto.getEbayFinding());
    highlightDto.setEbayFinding(persistedEbayFindingDto);
    ebayHighlightService.saveHighlight(highlightDto);
  }

  private static MerchandiseVerdictType determineInitialVerdictBasedOnListingType(ItemSummary itemSummary) {
    if (itemSummary.getBuyingOptions().contains("AUCTION")) {
      return MerchandiseVerdictType.SNIPE_RECOMMENDED;
    }
    if(itemSummary.getBuyingOptions().contains("FIXED_PRICE")) {
      return MerchandiseVerdictType.BUY_IT_NOW_RECOMMENDED;
    }
    throw new IllegalStateException("Unknown listing type: " + itemSummary.getBuyingOptions());
  }

  private EbayFindingDto saveOrUpdateEbayFinding(EbayFindingDto ebayFindingDto) {
    ebayFindingDto.setCapturedAt(Instant.now());
    if (ebayFindingDto.getId() == null) {
      return ebayFindingService.saveFinding(ebayFindingDto);
    } else {
      return ebayFindingService.updateFinding(ebayFindingDto);
    }
  }

  private EbayFindingDto getEbayFindingDto(ItemSummary itemSummary, EbaySellerDto ebaySellerDto) {
    return ebayFindingService.findByEbayId(itemSummary.getItemId())
        .orElseGet(() -> ebayFindingMapper.itemSummaryToEbayFindingDto(itemSummary, ebaySellerDto));
  }

  private static void applyDataExtractor(ItemSummary itemSummary,
      ItemSummaryDataExtractor itemSummaryDataExtractor, EbayHighlightDto highlightDto) {
    log.trace("Running extractor: {}", itemSummaryDataExtractor.getClass()
        .getSimpleName());
    if (itemSummaryDataExtractor.isApplicableTo(highlightDto)) {
      itemSummaryDataExtractor.extractDataFromItem(itemSummary, highlightDto);
    }
  }

  private static void applyMatcher(ProductVariantEnum targetProductVariant, ItemSummary itemSummary,
      ItemSummaryMatcher itemSummaryMatcher, EbayHighlightDto highlightDto) {
    log.trace("Running matcher: {}", itemSummaryMatcher.getClass().getSimpleName());
    if (itemSummaryMatcher.isApplicableTo(targetProductVariant)) {
      itemSummaryMatcher.match(itemSummary, highlightDto);
    }
    updateMerchandiseVerdict(highlightDto);
  }

  private ProductScrapeProcessor findMatchingScrapingProcessor(ProductVariantEnum targetProductVariant) {
    List<ProductScrapeProcessor> matchingProcessors = scrapeProcessors.stream()
        .filter(processor -> processor.supports(targetProductVariant))
        .toList();
    if (matchingProcessors.size() != 1) {
      String errorMessage = String.format("Expected 1 matching processor for item %s, found %s",
          targetProductVariant, matchingProcessors);
      log.error(errorMessage);
      throw new ScrapingException(errorMessage);
    }
    return matchingProcessors.get(0);
  }

  private static void updateMerchandiseVerdict(EbayHighlightDto highlightDto) {
    highlightDto.getPipelineMetadata()
        .filterReasoningsFromLog()
        .stream()
        .min(Comparator.comparing(merchandiseReasoningDto -> merchandiseReasoningDto.getVerdict()
            .ordinal()))
        .map(MerchandiseReasoningLog::getVerdict)
        .ifPresent(highlightDto::setAggregatedVerdict);
  }

}
