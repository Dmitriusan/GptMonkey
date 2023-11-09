package io.irw.hawk.scraper.service;

import io.irw.hawk.scraper.exceptions.ScrapingException;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
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
  List<ProductScrapeProcessor> scrapeProcessors;
  private AtomicBoolean isScraping = new AtomicBoolean(false);

  /**
   * The run ID is a timestamp with current time in millis
   */
  private String scrapeRunId = String.valueOf(System.currentTimeMillis());

  public void scrape() {
    isScraping.set(true);
    while (isScraping.get()) {
      scrapeTargetProviderService.getNextSearchTarget(scrapeRunId)
          .ifPresentOrElse(this::scrapePV, () -> isScraping.set(false));
    }
  }

  @NotNull
  private void scrapePV(ScrapeTargetDto scrapeTargetDto) {
    List<ProductScrapeProcessor> matchingProcessors = scrapeProcessors.stream()
        .filter(processor -> processor.supports(scrapeTargetDto))
        .toList();
    if (matchingProcessors.size() != 1) {
      String errorMessage = String.format("Expected 1 matching processor for item %s, found %s", scrapeTargetDto, matchingProcessors);
      log.error(errorMessage);
      throw new ScrapingException(errorMessage);
    }
    matchingProcessors.get(0).process(scrapeTargetDto);
  }

}
