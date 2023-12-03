package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.AUCTION;
import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.FIXED_PRICE;
import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.NOT_INTERESTING;
import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.UNPROCESSABLE;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantPreferencesDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningLog;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.domain.ShippingAndHandlingCostService;
import io.irw.hawk.scraper.service.matchers.BaselineItemDataMatcher;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import io.irw.hawk.scraper.service.processors.skates.parts.extractors.WheelCountExtractor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  ShippingAndHandlingCostService shippingAndHandlingCostService;
  static int DESIRED_MIN_WHEEL_COUNT = 4;

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(BaselineItemDataMatcher.class, WheelCountExtractor.class, WheelCountMatcher.class);
  }

  @Override
  public void match(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    MerchandiseVerdictType finalVerdict = highlightDto.getAggregatedVerdict();
    if (! finalVerdict.isBuyable()) {
      highlightDto.getPipelineMetadata()
          .addLog("Skipping item as it is not buyable (status=%s)".formatted(finalVerdict));
      return;
    }

    EbayFindingDto ebayFindingDto = highlightDto.getEbayFinding();

    if (ebayFindingDto.getBuyingOptions().contains(AUCTION)) {
      var currentAucPricePerPieceWithShippingUsd = ebayFindingDto.getCurrentAucPricePerPieceWithShippingUsd();
      List<MerchandiseReasoningLog> reasonings = checkPricePerPiece(highlightDto,
          currentAucPricePerPieceWithShippingUsd.get());
      reasonings.forEach(reasoning -> highlightDto.getPipelineMetadata().addReasoning(reasoning));
    } else if(ebayFindingDto.getBuyingOptions().contains(FIXED_PRICE)) {
      var buyNowPricePerPieceWithShippingUsd = ebayFindingDto.getBuyNowPricePerPieceWithShippingUsd();
      List<MerchandiseReasoningLog> reasonings = checkPricePerPiece(highlightDto, buyNowPricePerPieceWithShippingUsd.get());
      reasonings.forEach(reasoning -> highlightDto.getPipelineMetadata().addReasoning(reasoning));
    } else {
      addNewReasoning(highlightDto, "Unknown listing type", UNPROCESSABLE);
    }

    // TODO: Check if buy it now price at auction is ~2/3 of my desired price - recommend buying immediately
  }

  private List<MerchandiseReasoningLog> checkPricePerPiece(EbayHighlightDto highlightDto,
      BigDecimal pricePerPieceWithShippingUsd) {
    ProductVariantPreferencesDto productVariantPreferences = shippingAndHandlingCostService
        .resolveProductVariantPreferences(highlightDto);
    Optional<BigDecimal> meestShippingAndHandlingCost = shippingAndHandlingCostService
        .calculateMeestShippingAndHandlingCost(highlightDto, productVariantPreferences);
    BigDecimal desiredCostPerPieceUsd = productVariantPreferences.getDesiredCostPerPieceUsd();

    List<MerchandiseReasoningLog> result = new ArrayList<>();
    EbayFindingDto ebayFindingDto = highlightDto.getEbayFinding();
    int numberOfPieces = ebayFindingDto.getNumberOfPieces().get();

    if (pricePerPieceWithShippingUsd.compareTo(desiredCostPerPieceUsd) > 0) {
      addNewReasoning(highlightDto, String.format("Too pricey: %s$ per wheel > %s$", pricePerPieceWithShippingUsd,
          desiredCostPerPieceUsd), NOT_INTERESTING);
    } else if (numberOfPieces < DESIRED_MIN_WHEEL_COUNT) {
      BigDecimal priceBenefit = BigDecimal.valueOf(numberOfPieces)
          .multiply(desiredCostPerPieceUsd)
          .subtract((BigDecimal.valueOf(numberOfPieces).multiply(pricePerPieceWithShippingUsd)));
      if (priceBenefit.compareTo(meestShippingAndHandlingCost.get()) < 0) {
        addNewReasoning(highlightDto,
            String.format("Items are cheap, but too small quantity for Meest shipping&handling: %s$ price "
                + "benefit", priceBenefit), NOT_INTERESTING);
      }
    }
    return result;
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return productVariant.equals(ProductVariantEnum.LABEDA_80_MM_WHEELS);
  }
}
