package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
import io.irw.hawk.scraper.service.matchers.ShippingPossibilitiesMatcher;
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
public class PriceExtractor implements ItemSummaryDataExtractor {

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(PieceCountExtractor.class, ShippingCostExtractor.class, ShippingPossibilitiesMatcher.class);
  }

  @Override
  public boolean isApplicableTo(ScrapeTargetDto scrapeTargetDto) {
    return true;
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    metadata.setTotalPriceUsd(Float.valueOf(itemSummary.getPrice().getValue()));
    metadata.setPricePerPieceUsd(metadata.getNumberOfPieces()
        .flatMap(pieces -> metadata.getMinShippingCostUsd()
            .map(shippingCost -> (metadata.getTotalPriceUsd() + shippingCost) / pieces)));
  }
}
