package io.irw.hawk.scraper.service.processors.skates.parts;

import static io.irw.hawk.integration.ebay.model.EbayFilters.AUCTION_OR_BUY_NOW_OR_FIXED_PRICE;
import static io.irw.hawk.integration.ebay.model.EbayFilters.priceMax;
import static io.irw.hawk.scraper.model.ProcessingPipelineStep.sortOutDependencies;
import static io.irw.hawk.scraper.utils.PrettyPrinter.*;

import com.ebay.buy.browse.api.ItemSummaryApi;
import com.ebay.buy.browse.model.ItemSummary;
import com.ebay.buy.browse.model.SearchPagedCollection;
import io.irw.hawk.dto.ebay.SearchTermDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductQualifierEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.integration.ebay.buy.browse.ItemSummaryApiWrapper;
import io.irw.hawk.integration.ebay.buy.browse.ItemSummarySearchParameterDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ProcessingPipelineStepMetadataDto;
import io.irw.hawk.scraper.service.InMemoryResultStoreService;
import io.irw.hawk.scraper.service.extractors.ItemSummaryDataExtractor;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import io.irw.hawk.scraper.service.processors.ProductScrapeProcessor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LabedaWheelsScrapeProcessor implements ProductScrapeProcessor {

  ProcessingPipelineStep[] processingPipelineSteps;

  static ProductVariantEnum[] SUPPORTED_PVS = new ProductVariantEnum[]{
      ProductVariantEnum.LABEDA_80_MM_WHEELS
  };

  static ProductQualifierEnum[] SUPPORTED_PQS = new ProductQualifierEnum[]{
      ProductQualifierEnum.NEW_LABEDA_80_MM_WHEELS,
      ProductQualifierEnum.USED_LABEDA_80_MM_WHEELS
  };

  AtomicLong searchTermIdCounter = new AtomicLong(0);

  ItemSummaryApiWrapper itemSummaryApi;
  ItemSummaryApi itemApi;
  InMemoryResultStoreService inMemoryResultStoreService;

  @Override
  public boolean supports(ProductVariantEnum productVariant) {
    return Arrays.stream(SUPPORTED_PVS).anyMatch(pv -> pv.equals(productVariant));
  }

  @Override
  public void process(HawkScrapeRunDto hawkScrapeRunDto) {
    ProductVariantEnum scrapeTargetDto = hawkScrapeRunDto.getProductVariant();
    for (SearchTermDto searchTermDto : generateNewLabeda80mmWheelsSearch()) {
      SearchPagedCollection result = itemSummaryApi.search(searchTermDto.getSearchParams());

      for (ItemSummary itemSummary : result.getItemSummaries()) {
        MerchandiseMetadataDto metadata = new MerchandiseMetadataDto();
        metadata.setHawkScrapeRunDto(hawkScrapeRunDto);

        for (ProcessingPipelineStep pipelineStep : sortOutDependencies(processingPipelineSteps)) {
          if (pipelineStep instanceof ItemSummaryMatcher itemSummaryMatcher) {
            log.trace("Running matcher: {}", itemSummaryMatcher.getClass().getSimpleName());
            if (itemSummaryMatcher.isApplicableTo(scrapeTargetDto)) {
              List<MerchandiseReasoningDto> reasoning = itemSummaryMatcher.match(itemSummary, metadata);
              metadata.getReasoning().addAll(reasoning);
            }
            List<MerchandiseReasoningDto> reasoningWithoutMatcherRecord = metadata.getReasoning().stream()
                .filter(reasoning -> StringUtils.isEmpty(reasoning.getReasoningMatcher()))
                .toList();
            validateMatcherRecordFieldPresense(itemSummaryMatcher, reasoningWithoutMatcherRecord);
            updateMerchandiseVerdict(metadata);
            storeProcessingPipelineStepMetadata(pipelineStep, metadata);
          } else if (pipelineStep instanceof ItemSummaryDataExtractor itemSummaryDataExtractor) {
            log.trace("Running extractor: {}", itemSummaryDataExtractor.getClass().getSimpleName());
            if (itemSummaryDataExtractor.isApplicableTo(scrapeTargetDto)) {
              itemSummaryDataExtractor.extractDataFromItemSummary(itemSummary, metadata);
            }
          } else {
            throw new IllegalStateException("Unknown pipeline step: " + pipelineStep);
          }
        }

        printSplitter();
        prettyPrint(itemSummary, metadata);
      }
    }

  }

  private static void storeProcessingPipelineStepMetadata(ProcessingPipelineStep pipelineStep, MerchandiseMetadataDto metadata) {
    metadata.getProcessingPipelineStepMetadataDtos()
        .add(ProcessingPipelineStepMetadataDto.builder()
            .processingPipelineStep(pipelineStep.getClass())
            .build());
  }

  private static void validateMatcherRecordFieldPresense(ItemSummaryMatcher itemSummaryMatcher,
      List<MerchandiseReasoningDto> reasoningWithoutMatcherRecord) {
    if (! reasoningWithoutMatcherRecord.isEmpty()) {
      throw new IllegalStateException(String.format("Matcher %s has created reasoning without matcher "
          + "record: %s", itemSummaryMatcher.getClass().getName(), reasoningWithoutMatcherRecord));
    }
  }

  private static void updateMerchandiseVerdict(MerchandiseMetadataDto metadata) {
    MerchandiseVerdictType verdict = metadata.getReasoning().stream()
        .min(Comparator.comparing(merchandiseReasoningDto -> merchandiseReasoningDto.getVerdict().ordinal()))
        .map(MerchandiseReasoningDto::getVerdict)
        .orElse(MerchandiseVerdictType.BUYING_OPPORTUNITY);
    metadata.setFinalVerdict(verdict);
  }

  private List<SearchTermDto> generateNewLabeda80mmWheelsSearch() {
    return List.of(
        SearchTermDto.builder()
            .id(searchTermIdCounter.incrementAndGet())
            .searchParams(
                ItemSummarySearchParameterDto.builder()
                    .q("labeda wheels 80 mm")
                    .filter(StringUtils.joinWith(",",AUCTION_OR_BUY_NOW_OR_FIXED_PRICE, priceMax(50)))
                    .sort("price")
                    .build()
            )
            .build()
    );
  }
}
