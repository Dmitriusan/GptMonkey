package io.irw.hawk.dto.ebay;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link io.irw.hawk.entity.EbayFinding}
 */
@Value
@Builder
public class EbayFindingDto {

  Long id;
  String ebayIdStr;
  String legacyEbayIdStr;
  String itemTitle;
  String itemDescription;
  BigDecimal currentAuctionPriceUsd;
  BigDecimal finalPriceUsd;
  BigDecimal minShippingUsd;
  BigDecimal buyItNowPriceUsd;
  int bidCount;
  Instant capturedAt;
  Instant endsOn;
  EbayListingTypeEnum listingType;
  EbayListingStatusEnum listingStatusEnum;
  EbaySellerDto seller;

}