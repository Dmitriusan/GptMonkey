package io.irw.hawk.dto.merchandise;

import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HawkScrapeRunDto implements Serializable {

  Long id;
  HawkFlightDto hawkFlight;
  Instant startedAt;
  ProductVariantEnum productVariant;
}