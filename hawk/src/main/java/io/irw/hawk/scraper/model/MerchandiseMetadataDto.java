package io.irw.hawk.scraper.model;

import static io.irw.hawk.scraper.model.MerchandiseVerdictType.BUY_IT_NOW_RECOMMENDED;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
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
public class MerchandiseMetadataDto {

  HawkScrapeRunDto hawkScrapeRunDto;
  EbayFindingDto ebayFindingDto;
  MerchandiseVerdictType finalVerdict;
  @Default
  List<MerchandiseReasoningDto> reasoning = new ArrayList<>();
  /**
   * These metadata objects are stored mainly for debugging purposes (e.g. to understand what steps were actually
   * executed and in what order)
   */
  @Default
  List<ProcessingPipelineStepMetadataDto> processingPipelineStepMetadataDtos = new ArrayList<>();


}
