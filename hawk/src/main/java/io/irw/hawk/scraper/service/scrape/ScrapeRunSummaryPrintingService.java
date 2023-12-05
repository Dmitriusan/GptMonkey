package io.irw.hawk.scraper.service.scrape;

import io.irw.hawk.configuration.HawkProperties;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.scraper.service.domain.EbayFindingService;
import io.irw.hawk.scraper.service.domain.EbayHighlightService;
import io.irw.hawk.scraper.service.domain.EbayHighlightService.ScrapeRunSummaryDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class ScrapeRunSummaryPrintingService {

  EbayFindingService ebayFindingService;
  EbayHighlightService ebayHighlightService;
  HawkProperties hawkProperties;

  public void printScrapeRunSummary(HawkScrapeRunDto hawkScrapeRunDto) {
    ScrapeRunSummaryDto runSummary = ebayHighlightService.getScrapeRunSummary(hawkScrapeRunDto);
    Pair<List<EbayHighlightDto>, List<EbayHighlightDto>> topHighlights = ebayHighlightService.getTopHighlights(
        hawkScrapeRunDto);

    log.info(StringUtils.repeat("=", 80));
    log.info("Scrape run summary for Run ID {} ({})", hawkScrapeRunDto.getId(), hawkScrapeRunDto.getProductVariant());
    log.info("Total: {} items processed", runSummary.getTotalHighligts());

    logWarnings();

    runSummary.getVerdictCounts()
        .forEach((key, value) -> log.info("  {} items with verdict {}", value, key));

    List<EbayHighlightDto> topBuyNowHiglights = topHighlights.getFirst();
    List<EbayHighlightDto> topAucHiglights = topHighlights.getSecond();

    log.info("\n\n");
    log.info("  Top Buy Now highlights:");
    topBuyNowHiglights.forEach(this::printBuyNowHighlight);
    log.info("\n\n");
    log.info("  Top Auc highlights:");
    topAucHiglights.forEach(this::printAucHighlight);
  }

  private void printBuyNowHighlight(EbayHighlightDto highlightDto) {
    log.info("Name: {}", highlightDto.getEbayFinding().getTitle());
    log.info("  URL: {}", highlightDto.getEbayFinding().getWebUrl());
    log.info("  Price: {}, pieces: {}",
        highlightDto.getEbayFinding().getBuyNowPricePerPieceWithShippingUsd().map(Object::toString).orElse("N/A"),
        highlightDto.getEbayFinding().getNumberOfPieces().map(Object::toString).orElse("N/A"));
    log.info(" Possible profit: {} USD, {} %", highlightDto.getExpectedBuyNowProfitUsd().map(Object::toString).orElse("N/A"),
        highlightDto.getExpectedBuyNowProfitPct().map(Object::toString).orElse("N/A"));
    log.info(StringUtils.repeat("-", 80));
  }

  private void printAucHighlight(EbayHighlightDto highlightDto) {
    log.info("Name: {}", highlightDto.getEbayFinding().getTitle());
    log.info("  URL: {}", highlightDto.getEbayFinding().getWebUrl());
    log.info("  Price: {}, pieces: {}",
        highlightDto.getEbayFinding().getCurrentAucPricePerPieceWithShippingUsd().map(Object::toString).orElse("N/A"),
        highlightDto.getEbayFinding().getNumberOfPieces().map(Object::toString).orElse("N/A"));
    log.info(" Possible profit: {} USD, {} %", highlightDto.getPossibleAuctionProfitUsd().map(Object::toString).orElse("N/A"),
        highlightDto.getPossibleAuctionProfitPct().map(Object::toString).orElse("N/A"));
    log.info(StringUtils.repeat("-", 80));
  }

  private void logWarnings() {
    if (hawkProperties.someAiFeaturesAreDisabled()) {
      log.warn(" ATTENTION: some AI features are disabled");
    }

    if (hawkProperties.samplingIsEnabled()) {
      log.warn(" ATTENTION: sampling is enabled, not all entities are processed");
    }

    if (hawkProperties.subsetIsEnabled()) {
      log.warn(" ATTENTION: subsetting is enabled, not all product variants are processed");
    }
  }

}
