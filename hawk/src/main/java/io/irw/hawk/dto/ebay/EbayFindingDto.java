package io.irw.hawk.dto.ebay;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link io.irw.hawk.entity.EbayFinding}
 */
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class EbayFindingDto {

  Long id;
  String ebayIdStr;
  String legacyEbayIdStr;
  String itemTitle;

  Instant capturedAt;
  Instant endsOn;
  @Default
  List<EbayListingTypeEnum> listingTypes = new ArrayList<>();
  EbayListingStatusEnum listingStatusEnum;
  EbaySellerDto seller;

  Optional<String> itemDescription;
  Optional<Integer> numberOfPieces;
  Optional<Integer> bidCount;

  // Prices
  Optional<BigDecimal> currentAuctionPriceUsd;
  Optional<BigDecimal> buyItNowPriceUsd;
  Optional<BigDecimal> minShippingCostUsd;
  Optional<BigDecimal> buyNowPricePerPieceWithShippingUsd;
  Optional<BigDecimal> currentAucPricePerPieceWithShippingUsd;
  /**
   * Price when auction ends
   */
  Optional<BigDecimal> finalAuctionPriceWithoutShippingUsd;

}