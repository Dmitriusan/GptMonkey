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
  Instant capturedAt;
  String ebayIdStr;
  String legacyEbayIdStr;
  String title;
  String webUrl;

  Instant listingCreatedAt;
  Instant listingEndsAt; // Not implemented yet

  @Default
  List<EbayListingTypeEnum> listingTypes = new ArrayList<>();
  EbayListingStatusEnum listingStatus;
  EbaySellerDto seller;

  @Default
  List<String> imageUrls = new ArrayList<>();

  Boolean topRatedBuyingExperience;

  @Default
  Optional<String> itemDescription = Optional.empty();
  @Default
  Optional<Integer> numberOfPieces = Optional.empty();
  @Default
  Optional<Integer> bidCount = Optional.empty();

  // Prices
  @Default
  Optional<BigDecimal> currentAuctionPriceUsd = Optional.empty();
  @Default
  Optional<BigDecimal> buyItNowPriceUsd = Optional.empty();
  @Default
  Optional<BigDecimal> minShippingCostUsd = Optional.empty();
  @Default
  Optional<BigDecimal> buyNowPricePerPieceWithShippingUsd = Optional.empty();
  @Default
  Optional<BigDecimal> currentAucPricePerPieceWithShippingUsd = Optional.empty();
  /**
   * Price when auction ends
   */
  @Default
  Optional<BigDecimal> finalAuctionPriceWithoutShippingUsd = Optional.empty();

}