package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;

public interface ItemSummaryDataExtractor extends ProcessingPipelineStep {

  boolean isApplicableTo(ProductVariantEnum productVariantEnum);

  void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata);

}
