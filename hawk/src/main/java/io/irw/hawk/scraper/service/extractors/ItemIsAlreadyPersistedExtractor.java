package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.GroupEnum;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
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
public class ItemIsAlreadyPersistedExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(EbayHighlightDto highlightDto) {
    return true;
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    if (highlightDto.getEbayFinding().getId() != null) {
      addLogStatement(highlightDto, "Item is already present at the DB");
      highlightDto.setNewItem(false);
    } else {
      highlightDto.setNewItem(true);
    }
  }
}
