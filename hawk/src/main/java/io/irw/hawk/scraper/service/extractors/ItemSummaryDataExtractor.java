package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;

public interface ItemSummaryDataExtractor extends ProcessingPipelineStep {

  boolean isApplicableTo(EbayHighlightDto highlightDto);

  void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto);

}
