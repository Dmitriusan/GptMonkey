package io.irw.hawk.integration.ebay.buy.browse;

import com.ebay.buy.browse.api.ItemSummaryApi;
import com.ebay.buy.browse.invoker.ApiClient;
import com.ebay.buy.browse.model.SearchPagedCollection;
import io.irw.hawk.integration.ebay.model.EbayMarketplaceSpecificInfoDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.client.RestClientException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemSummaryApiWrapper extends ItemSummaryApi {

  private final EbayMarketplaceSpecificInfoDto marketplaceSpecificInfoDto;

  public ItemSummaryApiWrapper(ApiClient apiClient, EbayMarketplaceSpecificInfoDto marketplaceSpecificInfoDto) {
    super(apiClient);
    this.marketplaceSpecificInfoDto = marketplaceSpecificInfoDto;
  }


  public SearchPagedCollection search(ItemSummarySearchParameterDto itemSummarySearchParameterDto)
      throws RestClientException {
    return super.search(
        itemSummarySearchParameterDto.getAspectFilter(),
        itemSummarySearchParameterDto.getAutoCorrect(),
        itemSummarySearchParameterDto.getCategoryIds(),
        itemSummarySearchParameterDto.getCharityIds(),
        itemSummarySearchParameterDto.getCompatibilityFilter(),
        itemSummarySearchParameterDto.getEpid(),
        itemSummarySearchParameterDto.getFieldgroups(),
        itemSummarySearchParameterDto.getFilter(),
        itemSummarySearchParameterDto.getGtin(),
        itemSummarySearchParameterDto.getLimit(),
        itemSummarySearchParameterDto.getOffset(),
        itemSummarySearchParameterDto.getQ(),
        itemSummarySearchParameterDto.getSort(),
        marketplaceSpecificInfoDto.getEndUserCtx(),
        marketplaceSpecificInfoDto.getMarketplace().getMarketplaceId()
    );
  }
}
