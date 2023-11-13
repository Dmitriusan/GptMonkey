package io.irw.hawk.dto.ebay;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link io.irw.hawk.entity.EbayFinding}
 */
@Value
@Builder
public class EbaySellerDto implements Serializable {

  Long id;
  String ebayIdStr;
  Instant registeredOn;
  float reputationPercentage;
  int feedbackScore;

}