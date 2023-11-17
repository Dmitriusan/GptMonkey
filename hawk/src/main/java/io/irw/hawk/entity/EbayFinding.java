package io.irw.hawk.entity;

import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import io.irw.hawk.dto.ebay.EbayListingStatusEnum;
import io.irw.hawk.dto.ebay.EbayListingTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 * A small and partial reflection of eBay listings that has been processed by the app. These listings can be found
 * during multiple scrape runs, so this table may be updated multiple times.
 */
@Getter
@Setter
@Entity
@Table(name = "ebay_finding", schema = "merchandise_db")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EbayFinding {

  @Id
  @Column(name = "id", updatable = false)
  @SequenceGenerator(name = "ebay_finding_seq",
      sequenceName = "ebay_finding_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "ebay_finding_seq")
  Long id;

  @Column(name = "captured_at", nullable = false)
  private Instant capturedAt;

  @Column(name = "ebay_id_str", nullable = false, unique = true)
  @EqualsAndHashCode.Include
  private String ebayIdStr;

  @Column(name = "legacy_ebay_id_str", nullable = false)
  @EqualsAndHashCode.Include
  private String legacyEbayIdStr;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "web_url", nullable = false)
  private String webUrl;

  @Column(name = "listing_created_at", nullable = false)
  private Instant listingCreatedAt;

  @Column(name = "listing_ends_at")
  private Instant listingEndsAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "listing_types", nullable = false, columnDefinition = "ebay_listing_type[]")
  @Type(value=EnumArrayType.class,
      parameters = @Parameter(
          name = AbstractArrayType.SQL_ARRAY_TYPE,
          value = "ebay_listing_type"
      ))
  private EbayListingTypeEnum[] listingTypes;

  @Enumerated(EnumType.STRING)
  @Column(name = "listing_status", nullable = false, columnDefinition = "ebay_listing_status")
  @Type(PostgreSQLEnumType.class)
  private EbayListingStatusEnum listingStatus;

  @ManyToOne
  @JoinColumn(name = "seller_id", nullable = false)
  private EbaySeller seller;

  @Column(name = "images", columnDefinition = "varchar[]")
  @Type(value = StringArrayType.class)
  private String[] imageUrls;

  @Column(name = "top_rated_buying_experience", nullable = false)
  private Boolean topRatedBuyingExperience;

  @Column(name = "item_description")
  private String itemDescription;

  @Column(name = "number_of_pieces")
  private Integer numberOfPieces;

  @Column(name = "bid_count")
  private Integer bidCount;

  // Prices

  @Column(name = "current_auction_price_usd")
  private BigDecimal currentAuctionPriceUsd;

  @Column(name = "buy_it_now_price_usd")
  private BigDecimal buyItNowPriceUsd;

  @Column(name = "min_shipping_cost_usd")
  private BigDecimal minShippingCostUsd;

  @Column(name = "buy_now_price_pp_with_shipping_usd")
  private BigDecimal buyNowPricePerPieceWithShippingUsd;

  @Column(name = "current_auc_price_pp_with_shipping_usd")
  private BigDecimal currentAucPricePerPieceWithShippingUsd;

  /**
   * Price when auction ends
   */
  @Column(name = "final_auction_price_without_shipping_usd")
  private BigDecimal finalAuctionPriceWithoutShippingUsd;

}
