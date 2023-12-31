package io.irw.hawk.scraper.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessingPipelineStepMetadata {

  Class<? extends ProcessingPipelineStep> pipelineStepClass;
  @Default
  List<PipelineStepLog> log = new ArrayList<>();

}
