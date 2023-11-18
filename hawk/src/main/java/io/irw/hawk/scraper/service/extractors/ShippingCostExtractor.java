package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import java.math.BigDecimal;
import java.util.Comparator;
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
public class ShippingCostExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    Optional<BigDecimal> minShippingCost = Optional.ofNullable(itemSummary.getShippingOptions())
        .flatMap(shippingOptions -> shippingOptions.stream()
          .filter(
              so -> so.getShippingCost() != null) // Looks like shipping cost from Canada is CALCULATED/null
          .map(shippingOptionSummary -> Double.valueOf(shippingOptionSummary.getShippingCost().getValue()))
          .min(Comparator.naturalOrder()))
          .map(BigDecimal::valueOf);
    highlightDto.getEbayFinding().setMinShippingCostUsd(minShippingCost);
  }

}
