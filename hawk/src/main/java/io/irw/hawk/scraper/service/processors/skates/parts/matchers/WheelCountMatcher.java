package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.UNPROCESSABLE;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.matchers.BaselineItemDataMatcher;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import io.irw.hawk.scraper.service.processors.skates.parts.extractors.AiWheelCountExtractor;
import io.irw.hawk.scraper.service.processors.skates.parts.extractors.WheelCountExtractor;
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
    return List.of(WheelCountExtractor.class, AiWheelCountExtractor.class);
  }

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
    return List.of(BaselineItemDataMatcher.class);
  }

  @Override
  public void match(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    if (highlightDto.getEbayFinding().getNumberOfPieces().isEmpty()) {
      addNewReasoning(highlightDto, "Could not determine the number of wheels",
          UNPROCESSABLE);
    }
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return productVariant.equals(ProductVariantEnum.LABEDA_80_MM_WHEELS);
  }
}
