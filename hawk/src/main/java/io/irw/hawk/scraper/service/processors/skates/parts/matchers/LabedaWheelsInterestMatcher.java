package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.dto.ebay.EbayListingTypeEnum.AUCTION;
import static io.irw.hawk.dto.ebay.EbayListingTypeEnum.FIXED_PRICE;
import static io.irw.hawk.scraper.model.MerchandiseVerdictType.NOT_INTERESTING;
import static io.irw.hawk.scraper.model.MerchandiseVerdictType.UNPROCESSABLE;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
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

    EbayFindingDto ebayFindingDto = metadata.getEbayFindingDto();

    if (ebayFindingDto.getListingTypes().contains(AUCTION)) {
      var currentAucPricePerPieceWithShippingUsd = ebayFindingDto.getCurrentAucPricePerPieceWithShippingUsd();
      List<MerchandiseReasoningDto> reasonings = checkPricePerPiece(metadata,
          currentAucPricePerPieceWithShippingUsd.get());
      result.addAll(reasonings);
    } else if(itemSummary.getBuyingOptions().contains(FIXED_PRICE)) {
      var buyNowPricePerPieceWithShippingUsd = ebayFindingDto.getBuyNowPricePerPieceWithShippingUsd();
      List<MerchandiseReasoningDto> reasonings = checkPricePerPiece(metadata, buyNowPricePerPieceWithShippingUsd.get());
      result.addAll(reasonings);
    } else {
      result.add(newReasoningDto("Unknown listing type", UNPROCESSABLE));
    }

    // TODO: Check if buy it now price at auction is ~2/3 of my desired price - recommend buying immediately
    return result;
  }

  private List<MerchandiseReasoningDto> checkPricePerPiece(MerchandiseMetadataDto metadata,
      BigDecimal pricePerPieceWithShippingUsd) {
    List<MerchandiseReasoningDto> result = new ArrayList<>();
    EbayFindingDto ebayFindingDto = metadata.getEbayFindingDto();
    int numberOfPieces = ebayFindingDto.getNumberOfPieces().get();
    if (pricePerPieceWithShippingUsd.compareTo(DESIRED_PRICE_PER_WHEEL) > 0) {
      result.add(newReasoningDto(String.format("Too pricey: %s$ per wheel > %s$", pricePerPieceWithShippingUsd,
          DESIRED_PRICE_PER_WHEEL), NOT_INTERESTING));
    } else if (numberOfPieces < DESIRED_MIN_WHEEL_COUNT) {
      BigDecimal priceBenefit = BigDecimal.valueOf(numberOfPieces)
          .multiply(DESIRED_PRICE_PER_WHEEL)
          .subtract((BigDecimal.valueOf(numberOfPieces).multiply(pricePerPieceWithShippingUsd)));
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
