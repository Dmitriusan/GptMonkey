package io.irw.hawk.scraper.service.extractors;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayListingTypeEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.matchers.ShippingPossibilitiesMatcher;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
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
public class ListingTypeExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    EbayFindingDto ebayFindingDto = metadata.getEbayFindingDto();
    if (itemSummary.getBuyingOptions().contains("AUCTION")) {
      ebayFindingDto.getListingTypes().add(EbayListingTypeEnum.AUCTION);
    }

    if(itemSummary.getBuyingOptions().contains("FIXED_PRICE")) {
      ebayFindingDto.getListingTypes().add(EbayListingTypeEnum.FIXED_PRICE);
    }
  }

}
