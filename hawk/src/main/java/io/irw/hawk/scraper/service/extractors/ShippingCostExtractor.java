package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.GroupEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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
public class ShippingCostExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(ScrapeTargetDto scrapeTargetDto) {
    return true;
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    Optional<Double> minShippingCost = Optional.ofNullable(itemSummary.getShippingOptions())
        .flatMap(shippingOptions -> shippingOptions.stream()
          .filter(
              so -> so.getShippingCost() != null) // Looks like shipping cost from Canada is CALCULATED/null
          .map(shippingOptionSummary -> Double.valueOf(shippingOptionSummary.getShippingCost().getValue()))
          .min(Comparator.naturalOrder()));
    metadata.setMinShippingCostUsd(minShippingCost);
  }

}
