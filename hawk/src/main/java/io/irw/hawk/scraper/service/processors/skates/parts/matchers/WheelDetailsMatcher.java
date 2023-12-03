package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.REJECT;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.matchers.BaselineItemDataMatcher;
import io.irw.hawk.scraper.service.matchers.ItemSummaryMatcher;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WheelDetailsMatcher implements ItemSummaryMatcher {

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(WheelCountMatcher.class);
  }

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
    return List.of(BaselineItemDataMatcher.class);
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return productVariant.equals(ProductVariantEnum.LABEDA_80_MM_WHEELS);
  }

  @Override
  public void match(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    String title = highlightDto.getEbayFinding().getTitle().toLowerCase();
    String shortDescription = lowerCase(highlightDto.getEbayFinding()
        .getItemDescription()
        .orElse(""));
    String mergedTitleAndDescription = title + " "+ shortDescription;

    // Check for micro bearings
    if (checkForMicrobearings(mergedTitleAndDescription)) {
      addNewReasoning(highlightDto, "Microbearing wheels detected at '%s'".formatted(mergedTitleAndDescription),
          REJECT);
    }
  }

  protected static boolean checkForMicrobearings(String mergedTitleAndDescription) {
    String[] microBearingsPatterns = {
        "\\bmicrobearing",
        "\\bmicro\\s+bearing",
    };

    for (String patternStr : microBearingsPatterns) {
      Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(mergedTitleAndDescription);

      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

}
