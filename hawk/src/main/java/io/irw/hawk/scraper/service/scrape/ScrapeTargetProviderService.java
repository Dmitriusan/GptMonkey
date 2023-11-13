package io.irw.hawk.scraper.service.scrape;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import java.util.Optional;
import java.util.Queue;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScrapeTargetProviderService {
  ProductCatalogService productCatalogService;
  Queue<ProductVariantEnum> searchTargets;

  public ScrapeTargetProviderService(ProductCatalogService productCatalogService) {
    this.productCatalogService = productCatalogService;
    searchTargets = new java.util.LinkedList<>(productCatalogService.getProducts());
  }

  public Optional<ProductVariantEnum> getNextScrapeTarget() {
    return Optional.ofNullable(searchTargets.poll());
  }

}
