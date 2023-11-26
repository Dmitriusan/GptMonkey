package io.irw.hawk.scraper.service.extractors;

import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.AUCTION;
import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.BEST_OFFER;
import static io.irw.hawk.dto.ebay.EbayBuyingOptionEnum.FIXED_PRICE;
import static io.irw.hawk.dto.ebay.EbayListingStatusEnum.ACTIVE;

import com.ebay.buy.browse.model.Image;
import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayBuyingOptionEnum;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
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
  public boolean isApplicableTo(EbayHighlightDto highlightDto) {
    return true;
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    EbayFindingDto ebayFindingDto = highlightDto.getEbayFinding();
    ebayFindingDto.setListingStatus(ACTIVE);
    extractListingType(itemSummary, ebayFindingDto);
    extractAllImages(itemSummary, ebayFindingDto);
    if (ebayFindingDto.getBuyingOptions().contains(AUCTION)) {
      ebayFindingDto.setBidCount(Optional.of(itemSummary.getBidCount()));
    }
  }

  private static void extractAllImages(ItemSummary itemSummary, EbayFindingDto ebayFindingDto) {
    List<Image> additionalImages = Optional.ofNullable(itemSummary.getAdditionalImages())
        .orElse(List.of());
    List<String> allImageUrls = Stream.concat(
            Stream.of(itemSummary.getImage()), additionalImages.stream())
        .map(Image::getImageUrl)
        .toList();
    ebayFindingDto.setImageUrls(allImageUrls);
  }

  private static void extractListingType(ItemSummary itemSummary, EbayFindingDto ebayFindingDto) {
    if (itemSummary.getBuyingOptions().contains("AUCTION") && ! ebayFindingDto.getBuyingOptions().contains(AUCTION)) {
      ebayFindingDto.getBuyingOptions().add(AUCTION);
    }

    if (itemSummary.getBuyingOptions().contains("BEST_OFFER") && ! ebayFindingDto.getBuyingOptions().contains(BEST_OFFER)) {
      ebayFindingDto.getBuyingOptions().add(BEST_OFFER);
    }

    if(itemSummary.getBuyingOptions().contains("FIXED_PRICE") && ! ebayFindingDto.getBuyingOptions().contains(FIXED_PRICE)) {
      ebayFindingDto.getBuyingOptions().add(FIXED_PRICE);
    }
  }

}
