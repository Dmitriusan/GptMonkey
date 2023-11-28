package io.irw.hawk.scraper.service.processors.skates.parts.extractors;

import static org.apache.commons.lang3.StringUtils.lowerCase;

import com.ebay.buy.browse.model.ItemSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.scraper.ai.LlmQuery;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.openai.LlmQueryService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
  public List<Class<? extends ProcessingPipelineStep>> dependsOn() {
    return List.of(WheelCountExtractor.class);
  }

  @Override
  public void extractDataFromItem(ItemSummary itemSummary, EbayHighlightDto highlightDto) {
    if (highlightDto.getEbayFinding().getNumberOfPieces().isPresent()) {
      return;
    }

    String title = highlightDto.getEbayFinding().getTitle().toLowerCase();
    String shortDescription = lowerCase(highlightDto.getEbayFinding()
        .getItemDescription()
        .orElse(""));

    AtomicReference<Optional<Integer>> numberOfWheels = new AtomicReference<>(Optional.empty());

    FunctionExecutor functionExecutor = new FunctionExecutor(Collections.singletonList(ChatFunction.builder()
        .name("store_number_of_wheels")
        .description("Store the number of wheels as determined from item listing description")
        .executor(AiWheelCountCallbackRequest.class, request -> {
          numberOfWheels.set(Optional.ofNullable(request.getNumberOfWheels()));
          return new AiWheelCountCallbackResponse("ok");
        })
        .build()));

    llmQueryService.functionCall(LlmQuery.builder()
        .functionExecutor(functionExecutor)
        .chatCompletionRequest(LlmQuery.getGpt35Builder()
            .messages(List.of(new ChatMessage(ChatMessageRole.SYSTEM.value(), HOW_MANY_WHEELS_ARE_THERE),
                new ChatMessage(ChatMessageRole.USER.value(), title + "\n" + shortDescription)))
            .functions(functionExecutor.getFunctions())
            .functionCall(ChatCompletionRequestFunctionCall.of("{\"name\": \"store_number_of_wheels\"}"))
            .build())
        .build());

    highlightDto.getEbayFinding()
        .setNumberOfPieces(numberOfWheels.get());
  }

  @Value
  public static class AiWheelCountCallbackRequest {
    @JsonPropertyDescription("Relevant fragment of the listing description that specifies the number of wheels")
    @JsonProperty(required = true)
    public String excerpt;

    @JsonPropertyDescription("The number of wheels according to the listing description")
    @JsonProperty(required = true)
    public Integer numberOfWheels;
  }

  @Value
  public static class AiWheelCountCallbackResponse {
    public String status;
  }


}