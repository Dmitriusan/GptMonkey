package io.irw.hawk.scraper.service.scrape;

import static io.irw.hawk.scraper.model.ProcessingPipelineStep.linearExecutionGraphByDependencies;

import com.ebay.buy.browse.api.ItemSummaryApi;
import com.ebay.buy.browse.model.ItemSummary;
import com.ebay.buy.browse.model.SearchPagedCollection;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.dto.ebay.SearchTermDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.integration.ebay.buy.browse.ItemSummaryApiWrapper;
import io.irw.hawk.mapper.EbayFindingMapper;
import io.irw.hawk.mapper.EbaySellerMapper;
import io.irw.hawk.scraper.exceptions.ScrapingException;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ProcessingPipelineStepMetadataDto;
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
import org.apache.commons.lang3.StringUtils;
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

      for (ItemSummary itemSummary : result.getItemSummaries()) {
        processItemSummary(targetProductVariant, itemSummary, hawkScrapeRunDto);
      }
    }
  }

  private void processItemSummary(ProductVariantEnum targetProductVariant, ItemSummary itemSummary,
      HawkScrapeRunDto hawkScrapeRunDto) {


    EbaySellerDto ebaySellerDto = ebaySellerService.upsertSeller(
        ebaySellerMapper.sellerToEbaySellerDto(itemSummary.getSeller()));

    MerchandiseMetadataDto metadata = MerchandiseMetadataDto.builder()
        .hawkScrapeRunDto(hawkScrapeRunDto)
        .ebayFindingDto(getEbayFindingDto(itemSummary, ebaySellerDto))
        .finalVerdict(determineInitialVerdictBasedOnListingType(itemSummary))
        .build();

    for (ProcessingPipelineStep pipelineStep : linearExecutionGraphByDependencies(processingPipelineSteps)) {
      if (pipelineStep instanceof ItemSummaryMatcher itemSummaryMatcher) {
        applyMatcher(targetProductVariant, itemSummary, pipelineStep, itemSummaryMatcher, metadata);
        if (metadata.getFinalVerdict() == MerchandiseVerdictType.ITEM_ALREADY_PERSISTED) {
          break;
        }
      } else if (pipelineStep instanceof ItemSummaryDataExtractor itemSummaryDataExtractor) {
        applyDataExtractor(targetProductVariant, itemSummary, itemSummaryDataExtractor, metadata);
      } else {
        throw new IllegalStateException("Unknown pipeline step: " + pipelineStep);
      }
    }

    saveOrUpdateEbayFinding(metadata.getEbayFindingDto());
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

  private void saveOrUpdateEbayFinding(EbayFindingDto ebayFindingDto) {
    ebayFindingDto.setCapturedAt(Instant.now());
    if (ebayFindingDto.getId() == null) {
      ebayFindingService.saveFinding(ebayFindingDto);
    } else {
      ebayFindingService.updateFinding(ebayFindingDto);
    }
  }

  private EbayFindingDto getEbayFindingDto(ItemSummary itemSummary, EbaySellerDto ebaySellerDto) {
    return ebayFindingService.findByEbayId(itemSummary.getItemId())
        .orElseGet(() -> ebayFindingMapper.itemSummaryToEbayFindingDto(itemSummary, ebaySellerDto));
  }

  private static void applyDataExtractor(ProductVariantEnum targetProductVariant, ItemSummary itemSummary,
      ItemSummaryDataExtractor itemSummaryDataExtractor, MerchandiseMetadataDto metadata) {
    log.trace("Running extractor: {}", itemSummaryDataExtractor.getClass()
        .getSimpleName());
    if (itemSummaryDataExtractor.isApplicableTo(targetProductVariant)) {
      itemSummaryDataExtractor.extractDataFromItemSummary(itemSummary, metadata);
    }
  }

  private static void applyMatcher(ProductVariantEnum targetProductVariant, ItemSummary itemSummary,
      ProcessingPipelineStep pipelineStep, ItemSummaryMatcher itemSummaryMatcher, MerchandiseMetadataDto metadata) {
    log.trace("Running matcher: {}", itemSummaryMatcher.getClass().getSimpleName());
    if (itemSummaryMatcher.isApplicableTo(targetProductVariant)) {
      List<MerchandiseReasoningDto> reasoning = itemSummaryMatcher.match(itemSummary, metadata);
      metadata.getReasoning()
          .addAll(reasoning);
    }
    List<MerchandiseReasoningDto> reasoningWithoutMatcherRecord = metadata.getReasoning()
        .stream()
        .filter(reasoning -> StringUtils.isEmpty(reasoning.getReasoningMatcher()))
        .toList();
    validateMatcherRecordFieldPresense(itemSummaryMatcher, reasoningWithoutMatcherRecord);
    updateMerchandiseVerdict(metadata);
    storeProcessingPipelineStepMetadata(pipelineStep, metadata);
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
    ProductScrapeProcessor productScrapeProcessor = matchingProcessors.get(0);
    return productScrapeProcessor;
  }


  private static void storeProcessingPipelineStepMetadata(ProcessingPipelineStep pipelineStep,
      MerchandiseMetadataDto metadata) {
    metadata.getProcessingPipelineStepMetadataDtos()
        .add(ProcessingPipelineStepMetadataDto.builder()
            .processingPipelineStep(pipelineStep.getClass())
            .build());
  }

  private static void validateMatcherRecordFieldPresense(ItemSummaryMatcher itemSummaryMatcher,
      List<MerchandiseReasoningDto> reasoningWithoutMatcherRecord) {
    if (!reasoningWithoutMatcherRecord.isEmpty()) {
      throw new IllegalStateException(String.format("Matcher %s has created reasoning without matcher " + "record: %s",
          itemSummaryMatcher.getClass()
              .getName(), reasoningWithoutMatcherRecord));
    }
  }

  private static void updateMerchandiseVerdict(MerchandiseMetadataDto metadata) {
    MerchandiseVerdictType verdict = metadata.getReasoning()
        .stream()
        .min(Comparator.comparing(merchandiseReasoningDto -> merchandiseReasoningDto.getVerdict()
            .ordinal()))
        .map(MerchandiseReasoningDto::getVerdict)
        .orElse(MerchandiseVerdictType.BUYING_OPPORTUNITY);
    metadata.setFinalVerdict(verdict);
  }

}
