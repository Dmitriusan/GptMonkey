package io.irw.hawk.scraper.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeName("merchandiseReasoning")
public class MerchandiseReasoningLog extends PipelineStepLog {

  MerchandiseVerdictType verdict;
  String reason;

}
