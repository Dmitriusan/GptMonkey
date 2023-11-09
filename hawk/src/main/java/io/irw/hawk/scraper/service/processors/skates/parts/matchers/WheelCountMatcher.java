package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.scraper.model.MerchandiseVerdictType.UNPROCESSABLE;

import com.ebay.buy.browse.model.ItemSummary;
import com.ebay.buy.browse.model.ShippingOptionSummary;
import io.irw.hawk.dto.merchandise.GroupEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
import io.irw.hawk.scraper.service.extractors.ShippingCostExtractor;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import io.irw.hawk.scraper.service.processors.skates.parts.extractors.WheelCountExtractor;
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
public class WheelCountMatcher implements ItemSummaryMatcher {

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(WheelCountExtractor.class);
  }

  @Override
  public List<MerchandiseReasoningDto> match(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    List<MerchandiseReasoningDto> result = new ArrayList<>();
    if (metadata.getNumberOfPieces().isEmpty()) {
      result.add(newReasoningDto("Could not determine the number of wheels", UNPROCESSABLE));
    }
    return result;
  }

  @Override
  public boolean isApplicableTo(ScrapeTargetDto scrapeTargetDto) {
    return scrapeTargetDto.getProductVariant().equals(ProductVariantEnum.LABEDA_80_MM_WHEELS);
  }
}
