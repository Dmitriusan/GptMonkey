package io.irw.hawk.scraper.model;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScrapeTargetDto {

  ProductVariantEnum productVariant;
  String scrapeRunId;

}
