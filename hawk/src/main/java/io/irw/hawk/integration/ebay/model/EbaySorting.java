package io.irw.hawk.integration.ebay.model;

public class EbaySorting {

  /**
   * his sorts by price + shipping cost in descending order (highest price first). This sorting option (by price) is
   * only guaranteed to work correctly if the X-EBAY-C-ENDUSERCTX request header is used, with the contextualLocation
   * parameter being used to set the delivery country and postal code. Here is an example of how this header would be
   * used to do this (note the URL encoding):
   * <p/>
   * X-EBAY-C-ENDUSERCTX:contextualLocation=country=<country>,zip=<zipCode>
   */
  public static final String SORT_BY_PRICE_AND_SHIPPING = "-price";

}
