package io.irw.hawk.scraper.service.scrape;

import io.irw.hawk.configuration.HawkProperties;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class ProductCatalogService {

  HawkProperties hawkProperties;

  AtomicLong productVariantId = new AtomicLong();
  AtomicLong productQualifierId = new AtomicLong();

  public List<ProductVariantEnum> getProducts() {
    List<ProductVariantEnum> candidateValues = List.of(ProductVariantEnum.values());
    if (hawkProperties.getSubset().isEnabled()) {
      return candidateValues.stream()
              .filter(pv -> hawkProperties.getSubset().getProducts().getVariants().contains(pv))
              .toList();
    } else {
      return candidateValues;
    }
  }

}
