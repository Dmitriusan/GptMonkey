package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ScrapeTargetDto;

public interface ItemSummaryDataExtractor extends ProcessingPipelineStep {

  boolean isApplicableTo(ScrapeTargetDto scrapeTargetDto);

  void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata);

}
