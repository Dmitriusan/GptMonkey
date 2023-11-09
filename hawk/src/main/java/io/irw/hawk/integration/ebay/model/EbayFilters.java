package io.irw.hawk.integration.ebay.model;

public class EbayFilters {

  public static final String AUCTION_ONLY = "buyingOptions:{AUCTION}";
  public static final String AUCTION_OR_BUY_NOW = "buyingOptions:{AUCTION|BEST_OFFER}";
  public static final String AUCTION_OR_BUY_NOW_OR_FIXED_PRICE = "buyingOptions:{AUCTION|BEST_OFFER|FIXED_PRICE}";
  public static final String CONDITION_NEW = "conditions:{NEW}";
  public static final String CONDITION_NEW_OR_USED = "conditions:{NEW|USED}";
  public static final String CONDITION_NEW_OR_USED_OR_UNSPECIFIED = "conditions:{NEW|USED|UNSPECIFIED}";

  public static String priceRange(double minUsd, double maxUsd) {
    return String.format("price:[%.2f..%.2f],priceCurrency:USD", minUsd, maxUsd);
  }

  public static String priceMin(double minUsd) {
    return String.format("price:[%.2f],priceCurrency:USD", minUsd);
  }

  public static String priceMax(double maxUsd) {
    return String.format("price:[..%.2f],priceCurrency:USD", maxUsd);
  }

}
