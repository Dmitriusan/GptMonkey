package io.irw.hawk.integration.ebay.buy.browse;

import lombok.Builder;
import lombok.Data;

/**
 * https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search
 * <p>
 * You can also use any combination of the category_Ids, epid, and q fields. This gives you additional control over the
 * result set.
 */
@Data
@Builder
public class ItemSummarySearchParameterDto {

  /**
   * A string consisting of one or more keywords that are used to search for items on eBay. The keywords are handled as
   * follows: If the keywords are separated by a space, it is treated as an AND. In the following example, the query
   * returns items that have iphone AND ipad. /buy/browse/v1/item_summary/search?q=iphone ipad
   * <p>
   * If the keywords are input using parentheses and separated by a comma, or if they are URL-encoded, it is treated as
   * an OR. In the following examples, the query returns items that have iphone OR ipad.
   * /buy/browse/v1/item_summary/search?q=(iphone, ipad)
   */
  private String q;

  /**
   * This field lets you search by the Global Trade Item Number of the item as defined by https://www.gtin.info. You can
   * search only by UPC (Universal Product Code). If you have other formats of GTIN, you need to search by keyword.
   */
  private String gtin;

  /**
   * Note: Currently, you can pass in only one category ID per request.
   */
  private String categoryIds;


  /**
   * array of string
   */
  private String autoCorrect;

  /**
   * The ePID is the eBay product identifier of a product from the eBay product catalog. This field limits the results
   * to only items in the specified ePID.
   */
  private String epid;

  /**
   * array of FilterField https://developer.ebay.com/api-docs/buy/browse/types/cos:FilterField
   * https://developer.ebay.com/api-docs/buy/static/ref-buy-browse-filters.html
   */
  private String filter;
  private String aspectFilter;
  /**
   * Note: The only products supported are cars, trucks, and motorcycles.
   */
  private String compatibilityFilter;


  /**
   * This field is a comma separated list of values that lets you control what is returned in the response. The default
   * is MATCHING_ITEMS, which returns the items that match the keyword or category specified. The other values return
   * data that can be used to create histograms or provide additional information.
   */
  private String fieldgroups;


  @Builder.Default
  private String limit = "200";
  @Builder.Default
  private String offset = "0";

  public static final String FIELD_GROUPS_EXTENDED_AND_MATCHING_ITEMS ="EXTENDED,MATCHING_ITEMS";

  /**
   * array of SortField https://developer.ebay.com/api-docs/buy/browse/types/cos:SortField
   * https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search
   *
   * To sort in descending order, insert a hyphen (-) before the name of the sorting option
   */
  private String sort;

  // Ignored
  private String charityIds;

  /**
   * This header is required to support revenue sharing for eBay Partner Network and to improve the accuracy of shipping
   * and delivery time estimations.<br>For additional information, refer to <a
   * href=\"/api-docs/buy/static/api-browse.html#Headers\" target=\"_blank \">Use request headers</a>.
   */
  private String X_EBAY_C_ENDUSERCTX;


}
