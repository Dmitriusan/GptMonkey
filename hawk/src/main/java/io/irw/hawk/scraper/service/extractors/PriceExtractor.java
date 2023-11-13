package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.matchers.ShippingPossibilitiesMatcher;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PriceExtractor implements ItemSummaryDataExtractor {

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(PieceCountExtractor.class, ShippingCostExtractor.class, ShippingPossibilitiesMatcher.class);
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    double priceDoubleValue = Double.parseDouble(itemSummary.getPrice().getValue());
    metadata.setTotalPriceUsd(BigDecimal.valueOf(priceDoubleValue));
    metadata.setPricePerPieceUsd(metadata.getNumberOfPieces()
        .flatMap(pieces -> metadata.getMinShippingCostUsd()
            .map(shippingCost -> calculatePricePerPieceWithShipping(metadata, pieces, shippingCost))));
  }

  @NotNull
  private static BigDecimal calculatePricePerPieceWithShipping(MerchandiseMetadataDto metadata, Integer pieces, BigDecimal shippingCost) {
    return metadata.getTotalPriceUsd()
        .add(shippingCost)
        .divide(BigDecimal.valueOf(pieces), RoundingMode.HALF_UP);
  }
}
