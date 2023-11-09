package io.irw.hawk.scraper.service;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.ScrapeTargetDto;
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

  public Optional<ScrapeTargetDto> getNextSearchTarget(String scrapeRunId) {
    return Optional.ofNullable(searchTargets.poll())
        .map(pv -> ScrapeTargetDto.builder()
            .scrapeRunId(scrapeRunId)
            .productVariant(pv)
            .build());
  }

}
