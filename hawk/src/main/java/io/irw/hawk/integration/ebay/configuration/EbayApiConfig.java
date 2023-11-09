package io.irw.hawk.integration.ebay.configuration;

import static com.ebay.api.client.auth.oauth2.model.Environment.PRODUCTION;

import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import io.irw.hawk.integration.ebay.model.EbayMarketplaceEnum;
import io.irw.hawk.integration.ebay.model.EbayMarketplaceSpecificInfoDto;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EbayApiConfig {

  ResourceLoader resourceLoader;
  private static final List<String> SCOPE_LIST = Arrays.asList("https://api.ebay.com/oauth/api_scope");

  private static final String HEADER_X_EBAY_C_ENDUSERCTX = "X_EBAY_C_ENDUSERCTX";

  @Bean
  @Qualifier("ebayRestTemplate")
  @SneakyThrows
  public RestTemplate ebayRestTemplate() {
    Resource credsFile = resourceLoader.getResource("classpath:ebay-rest-config.yaml");
    InputStream credsFileIs = credsFile.getInputStream();
    CredentialUtil.load(credsFileIs);

    OAuth2Api oauth2Api = new OAuth2Api();
    //Only this scope is allowed for this app
    OAuthResponse oauth2Response = oauth2Api.getApplicationToken(PRODUCTION, SCOPE_LIST);
    Optional<AccessToken> applicationToken = oauth2Response.getAccessToken();

    // Base64 encode the client id and secret
    RestTemplate restTemplate = new RestTemplate();
    // Create and set authorization header
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + applicationToken.get().getToken());

    // Associate headers with RestTemplate
    restTemplate.getInterceptors().add((request, body, execution) -> {
      request.getHeaders().addAll(headers);
      return execution.execute(request, body);
    });

    return restTemplate;
  }

  @Bean
  public EbayMarketplaceSpecificInfoDto ebayMarketplaceSpecificInfo() {
    // For some reason, secondary "=" characters have to be encoded as "%3D" in the string
    return new EbayMarketplaceSpecificInfoDto(EbayMarketplaceEnum.EBAY_US, "contextualLocation=country%3DUS%2Czip%3D19808");
  }

}
