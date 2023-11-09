package io.irw.hawk.integration.ebay.model;

import lombok.Getter;

/**
 * @see <a href="https://developer.ebay.com/api-docs/commerce/identity/types/bas:MarketplaceIdEnum"/a>
 */
@Getter
public enum EbayMarketplaceEnum {
  EBAY_US("EBAY_US", "United States", "en-US", "https://www.ebay.com");

  private final String marketplaceId;
  private final String country;
  private final String localeSupported;
  private final String[] urls;

  EbayMarketplaceEnum(String marketplaceId, String country, String localeSupported, String... urls) {
    this.marketplaceId = marketplaceId;
    this.country = country;
    this.localeSupported = localeSupported;
    this.urls = urls;
  }

}
