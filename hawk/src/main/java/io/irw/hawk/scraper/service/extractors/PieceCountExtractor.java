package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.GroupEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PieceCountExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(ScrapeTargetDto scrapeTargetDto) {
    return ! scrapeTargetDto.getProductVariant().getGroup().equals(GroupEnum.WHEELS);
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    metadata.setNumberOfPieces(Optional.of(1));
  }
}
