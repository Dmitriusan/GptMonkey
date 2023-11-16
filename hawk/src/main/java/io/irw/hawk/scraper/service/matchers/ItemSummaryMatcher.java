package io.irw.hawk.scraper.service.matchers;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import java.util.List;

public interface ItemSummaryMatcher extends ProcessingPipelineStep {

  boolean isApplicableTo(ProductVariantEnum productVariant);

  List<MerchandiseReasoningDto> match(ItemSummary itemSummary, MerchandiseMetadataDto metadata);

  default MerchandiseReasoningDto newReasoningDto(String reason, MerchandiseVerdictType verdict) {
    return MerchandiseReasoningDto.builder()
        .reasoningMatcher(this.getClass().getSimpleName())
        .reason(reason)
        .verdict(verdict)
        .build();
  }

}
