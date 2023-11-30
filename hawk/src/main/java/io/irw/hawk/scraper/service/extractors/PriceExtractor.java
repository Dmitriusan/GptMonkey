package io.irw.hawk.scraper.service.extractors;

import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.AUCTION;
import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.FIXED_PRICE;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
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

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(BasicFieldExtractor.class, PieceCountExtractor.class, ShippingCostExtractor.class,
        ShippingPossibilitiesMatcher.class);
  }

  @Override
  public boolean isApplicableTo(EbayHighlightDto highlightDto) {
    return true;
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    EbayFindingDto ebayFindingDto = highlightDto.getEbayFinding();

    if (ebayFindingDto.getBuyingOptions().contains(AUCTION)) {
      var priceValue = BigDecimal.valueOf(Double.parseDouble(itemSummary.getCurrentBidPrice().getValue()));
      ebayFindingDto.setCurrentAuctionPriceUsd(Optional.of(priceValue));

      ebayFindingDto.setCurrentAucPricePerPieceWithShippingUsd(ebayFindingDto.getNumberOfPieces()
          .flatMap(pieces -> ebayFindingDto.getMinShippingCostUsd()
              .map(shippingCost -> calculatePricePerPieceWithShipping(priceValue, pieces, shippingCost))));

      addLogStatement(highlightDto,
          "Given # of pieces: %s, the current auc price per piece with shipping is %s".formatted(
              ebayFindingDto.getNumberOfPieces().map(String::valueOf).orElse("N/A"),
              ebayFindingDto.getCurrentAucPricePerPieceWithShippingUsd().map(String::valueOf).orElse("N/A")));
    }

    if(ebayFindingDto.getBuyingOptions().contains(FIXED_PRICE)) {
      var priceValue = BigDecimal.valueOf(Double.parseDouble(itemSummary.getPrice().getValue()));
      ebayFindingDto.setBuyItNowPriceUsd(Optional.of(priceValue));
      
      ebayFindingDto.setBuyNowPricePerPieceWithShippingUsd(ebayFindingDto.getNumberOfPieces()
          .flatMap(pieces -> ebayFindingDto.getMinShippingCostUsd()
              .map(shippingCost -> calculatePricePerPieceWithShipping(priceValue, pieces, shippingCost))));

      addLogStatement(highlightDto,
          "Given # of pieces: %s, the current \"buy it now\" price per piece with shipping is %s".formatted(
              ebayFindingDto.getNumberOfPieces().map(String::valueOf).orElse("N/A"),
              ebayFindingDto.getBuyNowPricePerPieceWithShippingUsd().map(String::valueOf).orElse("N/A")));
    }
  }

  @NotNull
  private static BigDecimal calculatePricePerPieceWithShipping(BigDecimal priceWithoutShipping, Integer pieces,
      BigDecimal shippingCost) {
    return priceWithoutShipping
        .add(shippingCost)
        .divide(BigDecimal.valueOf(pieces), RoundingMode.HALF_UP);
  }
}
