package io.irw.hawk.scraper.service.matchers;

import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.HUMAN_INTERVENTION_REQUIRED;
import static io.irw.hawk.dto.merchandise.MerchandiseVerdictType.REJECT;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
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
  public void match(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    EbayFindingDto ebayFindingDto = highlightDto.getEbayFinding();
    EbaySellerDto seller = ebayFindingDto.getSeller();

    float feedbackPercentage = seller.getReputationPercentage();
    if (feedbackPercentage < 80.0) {
      addNewReasoning(highlightDto, String.format("Seller has a bad reputation %s",
          feedbackPercentage), REJECT);
    }

    if (feedbackPercentage < 95.0 && ! ebayFindingDto.getTopRatedBuyingExperience()) {
      addNewReasoning(highlightDto, String.format("Seller has a shady reputation %s", feedbackPercentage),
          HUMAN_INTERVENTION_REQUIRED);
    }

    if (seller.getFeedbackScore() < 3) {
      addNewReasoning(highlightDto, String.format("Seller has a low feedback score %s", seller.getFeedbackScore()),
          HUMAN_INTERVENTION_REQUIRED);
    }
    addLogStatement(highlightDto, "The seller seems to have a good reputation");
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return true;
  }
}
