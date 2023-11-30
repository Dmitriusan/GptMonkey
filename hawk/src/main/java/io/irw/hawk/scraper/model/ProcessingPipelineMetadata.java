package io.irw.hawk.scraper.model;

import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
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
public class ProcessingPipelineMetadata {

  @Default
  List<ProcessingPipelineStepMetadata> pipelineStepMetadata = new ArrayList<>();

  public void newStep(Class<? extends ProcessingPipelineStep> processingPipelineStep) {
    pipelineStepMetadata.add(ProcessingPipelineStepMetadata.builder()
        .pipelineStepClass(processingPipelineStep)
        .build());
  }

  public void addReasoning(MerchandiseReasoningLog reasoningDto) {
    getLastPipelineStep().getLog().add(reasoningDto);
  }



  public void addLog(String message) {
    getLastPipelineStep().getLog().add(PipelineStepLog.builder()
        .message(message)
        .build());
  }

  public List<MerchandiseReasoningLog> filterReasoningsFromLog() { //retrieve from logs by instanceof
    return pipelineStepMetadata.stream()
        .flatMap(step -> step.getLog().stream())
        .filter(MerchandiseReasoningLog.class::isInstance)
        .map(MerchandiseReasoningLog.class::cast)
        .toList();
  }

  private ProcessingPipelineStepMetadata getLastPipelineStep() {
    return pipelineStepMetadata.get(pipelineStepMetadata.size() - 1);
  }

}
