package io.irw.hawk.integration.ebay.configuration;

import com.ebay.buy.browse.api.ItemApi;
import com.ebay.buy.browse.invoker.ApiClient;
import io.irw.hawk.integration.ebay.buy.browse.ItemSummaryApiWrapper;
import io.irw.hawk.integration.ebay.model.EbayMarketplaceSpecificInfoDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EbayBuyBrowseApiIntegrationConfig {

  EbayMarketplaceSpecificInfoDto marketplaceSpecificInfoDto;

  @Bean
  public ItemSummaryApiWrapper itemSummaryApi(@Qualifier("ebayRestTemplate") RestTemplate restTemplate) {
    return new ItemSummaryApiWrapper(apiClient(restTemplate), marketplaceSpecificInfoDto);
  }

  @Bean
  public ItemApi itemApi(@Qualifier("ebayRestTemplate") RestTemplate restTemplate) {
    return new ItemApi(apiClient(restTemplate));
  }

  @Bean
  public ApiClient apiClient(RestTemplate restTemplate) {
    return new ApiClient(restTemplate);
  }

}
