package io.irw.hawk.scraper.service;

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

  AtomicLong productVariantId = new AtomicLong();
  AtomicLong productQualifierId = new AtomicLong();

  public List<ProductVariantEnum> getProducts() {
    return List.of(ProductVariantEnum.values());
  }

}
