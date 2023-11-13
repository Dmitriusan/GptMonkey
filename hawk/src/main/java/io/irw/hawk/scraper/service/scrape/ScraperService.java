package io.irw.hawk.scraper.service.scrape;

import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.exceptions.ScrapingException;
import io.irw.hawk.scraper.service.domain.HawkScrapeRunService;
import io.irw.hawk.scraper.service.processors.ProductScrapeProcessor;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class ScraperService {

  ScrapeTargetProviderService scrapeTargetProviderService;
  HawkScrapeRunService hawkScrapeRunService;
  List<ProductScrapeProcessor> scrapeProcessors;
  private AtomicBoolean isScraping = new AtomicBoolean(false);

  public void scrape() {
    isScraping.set(true);
    while (isScraping.get()) {
      scrapeTargetProviderService.getNextScrapeTarget()
          .ifPresentOrElse(this::scrapePV, () -> isScraping.set(false));
    }
  }

  @NotNull
  private void scrapePV(ProductVariantEnum targetProductVariant) {
    HawkScrapeRunDto hawkScrapeRunDto = hawkScrapeRunService.startScrapeRun(targetProductVariant);

    List<ProductScrapeProcessor> matchingProcessors = scrapeProcessors.stream()
        .filter(processor -> processor.supports(targetProductVariant))
        .toList();
    if (matchingProcessors.size() != 1) {
      String errorMessage = String.format("Expected 1 matching processor for item %s, found %s",
          targetProductVariant, matchingProcessors);
      log.error(errorMessage);
      throw new ScrapingException(errorMessage);
    }
    matchingProcessors.get(0).process(hawkScrapeRunDto);
  }

}
