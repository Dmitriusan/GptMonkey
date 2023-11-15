package io.irw.hawk.dto.ebay;

import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.scraper.model.MerchandiseVerdictType;
import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link io.irw.hawk.entity.EbayHighlight}
 */
@Value
@Builder
public class EbayHighlightDto implements Serializable {

  Long id;
  HawkScrapeRunDto run;
  EbayFindingDto ebayFinding;
  MerchandiseVerdictType finalVerdict;
}