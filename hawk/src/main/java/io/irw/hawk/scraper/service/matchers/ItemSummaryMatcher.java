package io.irw.hawk.scraper.service.matchers;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
import java.util.List;

public interface ItemSummaryMatcher extends ProcessingPipelineStep {

  boolean isApplicableTo(ScrapeTargetDto scrapeTargetDto);

  default List<MerchandiseReasoningDto> match(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    return List.of();
  }

  default List<MerchandiseReasoningDto> postProcess(ItemSummary itemSummary) {
    return List.of();
  }

  default MerchandiseReasoningDto newReasoningDto(String reason, MerchandiseVerdictType verdict) {
    return MerchandiseReasoningDto.builder()
        .reasoningMatcher(this.getClass().getSimpleName())
        .reason(reason)
        .verdict(verdict)
        .build();
  }

}
