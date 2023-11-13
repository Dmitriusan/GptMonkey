package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.scraper.model.MerchandiseVerdictType.NOT_INTERESTING;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.matchers.BaselineItemDataMatcher;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import io.irw.hawk.scraper.service.processors.skates.parts.extractors.WheelCountExtractor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LabedaWheelsInterestMatcher implements ItemSummaryMatcher {

  static BigDecimal DESIRED_PRICE_PER_WHEEL = BigDecimal.valueOf(5);
  static int DESIRED_MIN_WHEEL_COUNT = 4;
  static BigDecimal MEEST_SHIPPING_AND_HANDLING_PER_SHIPPING = BigDecimal.valueOf(4);

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(BaselineItemDataMatcher.class, WheelCountExtractor.class, WheelCountMatcher.class);
  }

  @Override
  public List<MerchandiseReasoningDto> match(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    List<MerchandiseReasoningDto> result = new ArrayList<>();
    if (! metadata.getFinalVerdict().isBuyable()) {
      return result;
    }

    BigDecimal pricePerPieceUsd = metadata.getPricePerPieceUsd().get();
    int numberOfPieces = metadata.getNumberOfPieces().get();
    if (pricePerPieceUsd.compareTo(DESIRED_PRICE_PER_WHEEL) > 0) {
      result.add(newReasoningDto(String.format("Too pricey: %s$ per wheel > %s$", pricePerPieceUsd,
          DESIRED_PRICE_PER_WHEEL), NOT_INTERESTING));
    } else if (numberOfPieces < DESIRED_MIN_WHEEL_COUNT) {
      BigDecimal priceBenefit = BigDecimal.valueOf(numberOfPieces)
          .multiply(DESIRED_PRICE_PER_WHEEL)
          .subtract((BigDecimal.valueOf(numberOfPieces).multiply(pricePerPieceUsd)));
      if (priceBenefit.compareTo(MEEST_SHIPPING_AND_HANDLING_PER_SHIPPING) < 0) {
        result.add(newReasoningDto(
            String.format("Items are cheap, but too small quantity for Meest shipping&handling: %s$ price "
                + "benefit", priceBenefit), NOT_INTERESTING));
      }
    }
    return result;
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return productVariant.equals(ProductVariantEnum.LABEDA_80_MM_WHEELS);
  }
}
