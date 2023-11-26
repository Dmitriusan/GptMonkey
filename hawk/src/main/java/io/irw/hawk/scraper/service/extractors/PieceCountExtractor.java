package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.GroupEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
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
public class PieceCountExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(EbayHighlightDto highlightDto) {
    return ! extractProductGroup(highlightDto).equals(GroupEnum.WHEELS);
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    highlightDto.getEbayFinding().setNumberOfPieces(Optional.of(1));
  }
}
