package io.irw.hawk.scraper.service.processors.skates.parts.extractors;

import static org.apache.commons.lang3.StringUtils.lowerCase;

import com.ebay.buy.browse.model.ItemSummary;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.scraper.ai.LlmQuery;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.extractors.PriceExtractor;
import io.irw.hawk.scraper.service.openai.LlmQueryService;
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
public class AiWheelCountExtractor extends WheelCountExtractor {

  public static final String HOW_MANY_WHEELS_ARE_THERE = """
      How many inline skate wheels are sold in this Ebay listing\s
      according to the listing description provided by the user?""";
  LlmQueryService llmQueryService;

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
    return List.of(PriceExtractor.class);
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    String title = highlightDto.getEbayFinding().getTitle().toLowerCase();
    String shortDescription = lowerCase(highlightDto.getEbayFinding()
        .getItemDescription()
        .orElse(""));

    llmQueryService.chatCompletion(LlmQuery.builder()
        .messages(List.of(
            new ChatMessage(ChatMessageRole.SYSTEM.value(), HOW_MANY_WHEELS_ARE_THERE),
            new ChatMessage(ChatMessageRole.USER.value(), title + "\n" + shortDescription)))
        .build());

    highlightDto.getEbayFinding().setNumberOfPieces(numberOfWheels);
  }


}
