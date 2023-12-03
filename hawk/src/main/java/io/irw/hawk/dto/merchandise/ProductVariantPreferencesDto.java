package io.irw.hawk.dto.merchandise;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantPreferencesDto {

  ProductVariantEnum productVariant;
  BigDecimal desiredCostPerPieceUsd;
  BigDecimal typicalPricePerPieceUsd;
  BigDecimal meestHandlingPerPieceUsd;
  BigDecimal targetMarketPricePerPieceUsd;
  BigDecimal weightOfPieceKg;

}
