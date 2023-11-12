package io.irw.hawk.scraper.service.processors;

import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;

public interface ProductScrapeProcessor {

  boolean supports(ProductVariantEnum productVariant);

  void process(HawkScrapeRunDto hawkScrapeRunDto);

}
