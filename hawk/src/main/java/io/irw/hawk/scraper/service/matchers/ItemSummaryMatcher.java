package io.irw.hawk.scraper.service.matchers;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseReasoningLog;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;

public interface ItemSummaryMatcher extends ProcessingPipelineStep {

  boolean isApplicableTo(ProductVariantEnum productVariant);

  void match(ItemSummary itemSummary, EbayHighlightDto highlightDto);

  default void addNewReasoning(EbayHighlightDto highlightDto, String reason,
      MerchandiseVerdictType verdict) {
    highlightDto.getPipelineMetadata()
        .addReasoning(MerchandiseReasoningLog.builder()
            .reason(reason)
            .verdict(verdict)
            .build());
  }

}
