package io.irw.hawk.dto.ebay;

import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineMetadata;
import java.math.BigDecimal;
import java.util.Optional;
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
public class EbayHighlightDto {

  Long id;
  HawkScrapeRunDto run;
  EbayFindingDto ebayFinding;
  ProcessingPipelineMetadata pipelineMetadata;
  MerchandiseVerdictType aggregatedVerdict;
  MerchandiseVerdictType humanVerdict;
  Boolean newItem;

  @Default
  Optional<BigDecimal> expectedBuyNowProfitUsd = Optional.empty();
  @Default
  Optional<BigDecimal> expectedBuyNowProfitPct = Optional.empty();
  @Default
  Optional<BigDecimal> possibleAuctionProfitUsd = Optional.empty();
  @Default
  Optional<BigDecimal> possibleAuctionProfitPct = Optional.empty();
}
