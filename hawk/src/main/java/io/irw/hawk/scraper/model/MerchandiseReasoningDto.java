package io.irw.hawk.scraper.model;

import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchandiseReasoningDto {

  String reasoningMatcher;
  MerchandiseVerdictType verdict;
  String reason;

}
