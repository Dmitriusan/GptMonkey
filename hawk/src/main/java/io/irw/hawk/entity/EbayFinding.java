package io.irw.hawk.entity;

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

  @Column(name = "ebay_id_str", nullable = false, unique = true)
  @EqualsAndHashCode.Include
  private String ebayIdStr;

  @Column(name = "legacy_ebay_id_str")
  @EqualsAndHashCode.Include
  private String legacyEbayIdStr;

  @Column(name = "item_title", nullable = false)
  private String itemTitle;

  @Column(name = "item_description")
  private String itemDescription;

  @Column(name = "current_auction_price_usd")
  private BigDecimal currentAuctionPriceUsd;

  @Column(name = "final_price_usd")
  private BigDecimal finalPriceUsd;

  @Column(name = "min_shipping_usd")
  private BigDecimal minShippingUsd;

  @Column(name = "buy_it_now_price_usd")
  private BigDecimal buyItNowPriceUsd;

  @Column(name = "bid_count")
  private int bidCount;

  @Column(name = "captured_at", nullable = false)
  private Instant capturedAt;

  @Column(name = "ends_on", nullable = false)
  private Instant endsOn;

  @Enumerated(EnumType.STRING)
  @Column(name = "listing_type", nullable = false, columnDefinition = "ebay_listing_type")
  private EbayListingTypeEnum listingType;

  @Enumerated(EnumType.STRING)
  @Column(name = "listing_status", nullable = false, columnDefinition = "ebay_listing_status")
  private EbayListingStatusEnum listingStatusEnum;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private EbaySeller seller;

}
