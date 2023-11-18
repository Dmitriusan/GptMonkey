package io.irw.hawk.scraper.service.matchers;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.domain.EbayFindingService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Virtual matcher that checks that basic fields are populated. This matcher is intended to be used as a common
 * dependency for more advanced matchers (instead of listing all other dependencies separately)
 */
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemAlreadyPersistedDataMatcher implements ItemSummaryMatcher {

  EbayFindingService ebayFindingService;

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(BaselineItemDataMatcher.class);
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }

  @Override
  public void match(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    if (highlightDto.getEbayFinding().getId() != null) {
      log.debug("Skipping item {} as it is already present at the DB", itemSummary.getItemId());
      addNewReasoning(highlightDto, "Item %s is already present at the DB".formatted(itemSummary.getItemId()),
          MerchandiseVerdictType.ITEM_ALREADY_PERSISTED);
    }
  }
}
