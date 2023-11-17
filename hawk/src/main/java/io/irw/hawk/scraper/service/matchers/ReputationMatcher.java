package io.irw.hawk.scraper.service.matchers;

import static io.irw.hawk.scraper.model.MerchandiseVerdictType.HUMAN_INTERVENTION_REQUIRED;
import static io.irw.hawk.scraper.model.MerchandiseVerdictType.REJECT;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.MerchandiseReasoningDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReputationMatcher implements ItemSummaryMatcher {

  @Override
  public List<MerchandiseReasoningDto> match(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    EbayFindingDto ebayFindingDto = metadata.getEbayFindingDto();
    EbaySellerDto seller = ebayFindingDto.getSeller();
    List<MerchandiseReasoningDto> result = new ArrayList<>();

    float feedbackPercentage = seller.getReputationPercentage();
    if (feedbackPercentage < 80.0) {
      result.add(newReasoningDto(String.format("Seller has a bad reputation %s", feedbackPercentage), REJECT));
    }

    if (feedbackPercentage < 95.0 && ! ebayFindingDto.getTopRatedBuyingExperience()) {
      result.add(newReasoningDto(String.format("Seller has a shady reputation %s", feedbackPercentage),
          HUMAN_INTERVENTION_REQUIRED));
    }

    if (seller.getFeedbackScore() < 3) {
      result.add(newReasoningDto(String.format("Seller has a low feedback score %s", seller.getFeedbackScore()),
          HUMAN_INTERVENTION_REQUIRED));
    }

    return result;
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }
}
