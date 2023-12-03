package io.irw.hawk.scraper.service.extractors;

import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.AUCTION;
import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.FIXED_PRICE;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantPreferencesDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.domain.ShippingAndHandlingCostService;
import io.irw.hawk.scraper.service.matchers.ShippingPossibilitiesMatcher;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
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

  ShippingAndHandlingCostService shippingAndHandlingCostService;

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(BasicFieldExtractor.class, PieceCountExtractor.class, ShippingCostExtractor.class,
        ShippingPossibilitiesMatcher.class);
  }

  @Override
  public boolean isApplicableTo(EbayHighlightDto highlightDto) {
    boolean numberOfPiecesIsKnown = highlightDto.getEbayFinding()
        .getNumberOfPieces()
        .isPresent();
    if (! numberOfPiecesIsKnown) {
      addLogStatement(highlightDto, "Skipping item as the number of pieces is not known");
    }
    return numberOfPiecesIsKnown;
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    EbayFindingDto ebayFindingDto = highlightDto.getEbayFinding();

    ProductVariantPreferencesDto productVariantPreferences =
        shippingAndHandlingCostService.resolveProductVariantPreferences(highlightDto);

    BigDecimal meestShippingAndHandlingOverhead = shippingAndHandlingCostService
        .calculateMeestShippingAndHandlingCost(highlightDto, productVariantPreferences).get();

    if (ebayFindingDto.getBuyingOptions().contains(AUCTION)) {
      var priceValue = BigDecimal.valueOf(Double.parseDouble(itemSummary.getCurrentBidPrice().getValue()));
      ebayFindingDto.setCurrentAuctionPriceUsd(Optional.of(priceValue));

      ebayFindingDto.setCurrentAucPricePerPieceWithShippingUsd(ebayFindingDto.getNumberOfPieces()
          .flatMap(pieces -> ebayFindingDto.getMinShippingCostUsd()
              .map(shippingCost -> calculatePricePerPieceWithShipping(priceValue, pieces, shippingCost))));

      addLogStatement(highlightDto,
          "Given the # of pieces: %s, the current auc price per piece with shipping is %s".formatted(
              ebayFindingDto.getNumberOfPieces().map(String::valueOf).orElse("N/A"),
              ebayFindingDto.getCurrentAucPricePerPieceWithShippingUsd().map(String::valueOf).orElse("N/A")));

      BigDecimal expectedProfitUsd = calculateExpectedProfitUsd(
          ebayFindingDto.getCurrentAucPricePerPieceWithShippingUsd().get(),
          productVariantPreferences.getTargetMarketPricePerPieceUsd(),
          ebayFindingDto.getNumberOfPieces().get(),
          meestShippingAndHandlingOverhead);
      highlightDto.setPossibleAuctionProfitUsd(Optional.of(expectedProfitUsd));

      BigDecimal expectedProfitPct = calculateExpectedProfitPct(expectedProfitUsd,
          ebayFindingDto.getCurrentAuctionPriceUsd().get(),
          ebayFindingDto.getMinShippingCostUsd().get(), meestShippingAndHandlingOverhead);
      highlightDto.setPossibleAuctionProfitPct(Optional.of(expectedProfitPct));
    }

    if(ebayFindingDto.getBuyingOptions().contains(FIXED_PRICE)) {
      var priceValue = BigDecimal.valueOf(Double.parseDouble(itemSummary.getPrice().getValue()));
      ebayFindingDto.setBuyItNowPriceUsd(Optional.of(priceValue));

      ebayFindingDto.setBuyNowPricePerPieceWithShippingUsd(ebayFindingDto.getNumberOfPieces()
          .flatMap(pieces -> ebayFindingDto.getMinShippingCostUsd()
              .map(shippingCost -> calculatePricePerPieceWithShipping(priceValue, pieces, shippingCost))));

      addLogStatement(highlightDto,
          "Given the # of pieces: %s, the current \"buy it now\" price per piece with shipping is %s".formatted(
              ebayFindingDto.getNumberOfPieces().map(String::valueOf).orElse("N/A"),
              ebayFindingDto.getBuyNowPricePerPieceWithShippingUsd().map(String::valueOf).orElse("N/A")));

      BigDecimal expectedProfitUsd = calculateExpectedProfitUsd(
          ebayFindingDto.getBuyNowPricePerPieceWithShippingUsd().get(),
          productVariantPreferences.getTargetMarketPricePerPieceUsd(),
          ebayFindingDto.getNumberOfPieces().get(),
          meestShippingAndHandlingOverhead);
      highlightDto.setExpectedBuyNowProfitUsd(Optional.of(expectedProfitUsd));

      BigDecimal expectedProfitPct = calculateExpectedProfitPct(expectedProfitUsd,
          ebayFindingDto.getBuyItNowPriceUsd().get(),
          ebayFindingDto.getMinShippingCostUsd().get(), meestShippingAndHandlingOverhead);
      highlightDto.setExpectedBuyNowProfitPct(Optional.of(expectedProfitPct));
    }
  }

  @NotNull
  private static BigDecimal calculatePricePerPieceWithShipping(BigDecimal priceWithoutShipping, Integer pieces,
      BigDecimal shippingCost) {
    return priceWithoutShipping
        .add(shippingCost)
        .divide(BigDecimal.valueOf(pieces), RoundingMode.HALF_UP);
  }

  private static BigDecimal calculateExpectedProfitUsd(BigDecimal pricePerPieceWithShipping,
      BigDecimal referencePricePerPiece, Integer pieces,
      BigDecimal meestShippingAndHandlingOverhead) {
    return referencePricePerPiece
        .subtract(pricePerPieceWithShipping)
        .multiply(BigDecimal.valueOf(pieces))
        .subtract(meestShippingAndHandlingOverhead);
  }

  private static BigDecimal calculateExpectedProfitPct(BigDecimal expectedProfitUsd, BigDecimal listingPriceUsd,
      BigDecimal shippingPriceUsd,
      BigDecimal meestShippingAndHandlingOverhead) {
    BigDecimal totalExpencesForListing = listingPriceUsd
        .add(shippingPriceUsd)
        .add(meestShippingAndHandlingOverhead);
    return expectedProfitUsd.divide(totalExpencesForListing, RoundingMode.HALF_DOWN);
  }
}
