package io.irw.hawk.scraper.service.processors.skates.parts;

import static io.irw.hawk.integration.ebay.buy.browse.ItemSummarySearchParameterDto.FIELD_GROUPS_EXTENDED_AND_MATCHING_ITEMS;
import static io.irw.hawk.integration.ebay.model.EbayFilters.AUCTION_OR_BUY_NOW_OR_FIXED_PRICE;
import static io.irw.hawk.integration.ebay.model.EbayFilters.priceMax;

import io.irw.hawk.dto.ebay.SearchTermDto;
import io.irw.hawk.dto.merchandise.ProductQualifierEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.integration.ebay.buy.browse.ItemSummarySearchParameterDto;
import io.irw.hawk.scraper.service.processors.ProductScrapeProcessor;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LabedaWheelsScrapeProcessor implements ProductScrapeProcessor {


  static ProductVariantEnum[] SUPPORTED_PVS = new ProductVariantEnum[]{ProductVariantEnum.LABEDA_80_MM_WHEELS};

  static ProductQualifierEnum[] SUPPORTED_PQS = new ProductQualifierEnum[]{ProductQualifierEnum.NEW_LABEDA_80_MM_WHEELS,
      ProductQualifierEnum.USED_LABEDA_80_MM_WHEELS};

  @Override
  public boolean supports(ProductVariantEnum productVariant) {
    return Arrays.stream(SUPPORTED_PVS)
        .anyMatch(pv -> pv.equals(productVariant));
  }

  @Override
  public List<SearchTermDto> generateSearchTerms(ProductVariantEnum productVariant) {
    return List.of(SearchTermDto.builder()
        .searchParams(ItemSummarySearchParameterDto.builder()
            .q("labeda wheels 80 mm")
            .filter(StringUtils.joinWith(",", AUCTION_OR_BUY_NOW_OR_FIXED_PRICE, priceMax(50)))
            .fieldgroups(FIELD_GROUPS_EXTENDED_AND_MATCHING_ITEMS)
            .sort("price")
            .build())
        .build());
  }

}
