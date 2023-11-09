package io.irw.hawk.integration.ebay.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EbayMarketplaceSpecificInfoDto {

  EbayMarketplaceEnum marketplace;

  /**
   * @see <a href="https://developer.ebay.com/api-docs/buy/static/api-browse.html#Headers"/a>
   */
  String endUserCtx;

}
