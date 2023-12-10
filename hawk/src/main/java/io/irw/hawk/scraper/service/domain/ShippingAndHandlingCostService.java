package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.dto.merchandise.ProductVariantPreferencesDto;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class ShippingAndHandlingCostService {

  static final BigDecimal MEEST_SHIPPING_PRICE_PER_KG_USD_AIR = BigDecimal.valueOf(8.75);

  public ProductVariantPreferencesDto resolveProductVariantPreferences(EbayHighlightDto highlightDto) {
    ProductVariantEnum productVariant = highlightDto.getRun()
        .getProductVariant();

    switch (productVariant) {
      case LABEDA_80_MM_WHEELS:
        return ProductVariantPreferencesDto.builder()
            .productVariant(productVariant)
            .desiredCostPerPieceUsd(BigDecimal.valueOf(5))
            .typicalPricePerPieceUsd(BigDecimal.valueOf(7))
            .meestHandlingPerPieceUsd(BigDecimal.valueOf(2))
            .targetMarketPricePerPieceUsd(BigDecimal.valueOf(7.5))
            .weightOfPieceKg(BigDecimal.valueOf(0.15))
            .build();
      case UNDERCOVER_80_MM_WHEELS:
        return ProductVariantPreferencesDto.builder()
                .productVariant(productVariant)
                .desiredCostPerPieceUsd(BigDecimal.valueOf(7))
                .typicalPricePerPieceUsd(BigDecimal.valueOf(15))
                .meestHandlingPerPieceUsd(BigDecimal.valueOf(2))
                .targetMarketPricePerPieceUsd(BigDecimal.valueOf(15))
                .weightOfPieceKg(BigDecimal.valueOf(0.15))
                .build();
      case UNDERCOVER_110_MM_WHEELS:
        return ProductVariantPreferencesDto.builder()
                .productVariant(productVariant)
                .desiredCostPerPieceUsd(BigDecimal.valueOf(8))
                .typicalPricePerPieceUsd(BigDecimal.valueOf(15))
                .meestHandlingPerPieceUsd(BigDecimal.valueOf(2))
                .targetMarketPricePerPieceUsd(BigDecimal.valueOf(17.3))
                .weightOfPieceKg(BigDecimal.valueOf(0.15))
                .build();
      default:
        throw new IllegalArgumentException("Unsupported product variant: " + productVariant);
    }
  }

  public Optional<BigDecimal> calculateMeestShippingAndHandlingCost(EbayHighlightDto highlightDto,
      ProductVariantPreferencesDto productVariantPreferencesDto) {
    return highlightDto.getEbayFinding().getNumberOfPieces()
        .map(pieces -> BigDecimal.valueOf(pieces)
            .multiply(productVariantPreferencesDto.getWeightOfPieceKg())
            .multiply(MEEST_SHIPPING_PRICE_PER_KG_USD_AIR)
            .add(productVariantPreferencesDto.getMeestHandlingPerPieceUsd()))
        .map(cost -> cost.setScale(2, BigDecimal.ROUND_HALF_UP));
  }

}
