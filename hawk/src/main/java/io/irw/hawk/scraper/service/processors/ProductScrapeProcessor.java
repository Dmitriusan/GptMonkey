package io.irw.hawk.scraper.service.processors;

import io.irw.hawk.scraper.model.ScrapeTargetDto;

public interface ProductScrapeProcessor {

  boolean supports(ScrapeTargetDto scrapeTargetDto);

  void process(ScrapeTargetDto scrapeTargetDto);

}
