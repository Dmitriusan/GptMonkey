package io.irw.hawk.scraper.service.matchers;

import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.UNPROCESSABLE;

import com.ebay.buy.browse.model.ItemSummary;
import com.ebay.buy.browse.model.ShippingOptionSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningLog;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.extractors.ShippingCostExtractor;
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
public class ShippingPossibilitiesMatcher implements ItemSummaryMatcher {

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(ShippingCostExtractor.class);
  }

  @Override
  public void match(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    List<MerchandiseReasoningLog> result = new ArrayList<>();
    List<ShippingOptionSummary> shippingOptions = itemSummary.getShippingOptions();

    if (shippingOptions == null || shippingOptions.isEmpty() ||
        highlightDto.getEbayFinding().getMinShippingCostUsd().isEmpty()) {
      addNewReasoning(highlightDto, String.format("The item has no shipping cost: shippingOptions=%s", shippingOptions),
          UNPROCESSABLE);
    }
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }
}
