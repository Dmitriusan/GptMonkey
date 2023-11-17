package io.irw.hawk.scraper.service.extractors;

import static io.irw.hawk.dto.ebay.EbayListingStatusEnum.ACTIVE;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayListingStatusEnum;
import io.irw.hawk.dto.ebay.EbayListingTypeEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BasicFieldExtractor implements ItemSummaryDataExtractor {

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    EbayFindingDto ebayFindingDto = metadata.getEbayFindingDto();
    ebayFindingDto.setListingStatus(ACTIVE);
    extractListingType(itemSummary, ebayFindingDto);
    extractAllImages(itemSummary, ebayFindingDto);
    if (ebayFindingDto.getListingTypes().contains(EbayListingTypeEnum.AUCTION)) {
      ebayFindingDto.setBidCount(Optional.of(itemSummary.getBidCount()));
    }
  }

  private static void extractAllImages(ItemSummary itemSummary, EbayFindingDto ebayFindingDto) {
    List<String> allImageUrls = Stream.concat(
            Stream.of(itemSummary.getImage()),
            itemSummary.getAdditionalImages().stream())
        .map(image -> image.getImageUrl())
        .toList();
    ebayFindingDto.setImageUrls(allImageUrls);
  }

  private static void extractListingType(ItemSummary itemSummary, EbayFindingDto ebayFindingDto) {
    if (itemSummary.getBuyingOptions().contains("AUCTION")) {
      ebayFindingDto.getListingTypes().add(EbayListingTypeEnum.AUCTION);
    }

    if (itemSummary.getBuyingOptions().contains("BEST_OFFER")) {
      ebayFindingDto.getListingTypes().add(EbayListingTypeEnum.BEST_OFFER);
    }

    if(itemSummary.getBuyingOptions().contains("FIXED_PRICE")) {
      ebayFindingDto.getListingTypes().add(EbayListingTypeEnum.FIXED_PRICE);
    }
  }

}
