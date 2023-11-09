package io.irw.hawk.dto.merchandise;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link io.irw.hawk.entity.Item}
 */
/**
 * DTO for {@link io.irw.hawk.entity.Item}
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

  long id;
  String ebayId;
  String ebayLegacyId;
  String itemName;
  float priceUsd;

}
