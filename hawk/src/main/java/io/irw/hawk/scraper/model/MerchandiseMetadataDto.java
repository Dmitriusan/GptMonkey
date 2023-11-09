package io.irw.hawk.scraper.model;

import static io.irw.hawk.scraper.model.MerchandiseVerdictType.BUY;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
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

  ScrapeTargetDto scrapeTarget;
  @Default
  Optional<Integer> numberOfPieces = Optional.of(1);
  Optional<Double> pricePerPieceUsd;
  double totalPriceUsd;
  Optional<Double> minShippingCostUsd;
  @Default
  MerchandiseVerdictType finalVerdict = BUY;
  @Default
  List<MerchandiseReasoningDto> reasoning = new ArrayList<>();


}
