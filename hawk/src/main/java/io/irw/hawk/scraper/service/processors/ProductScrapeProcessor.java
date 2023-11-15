package io.irw.hawk.scraper.service.processors;

import io.irw.hawk.dto.ebay.SearchTermDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import java.util.List;

public interface ProductScrapeProcessor {

  boolean supports(ProductVariantEnum productVariant);

  List<SearchTermDto> generateSearchTerms(ProductVariantEnum productVariant);

}
