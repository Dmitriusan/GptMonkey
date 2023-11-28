package io.irw.hawk.scraper.service.openai;

import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.irw.hawk.scraper.ai.LlmQuery;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class LlmQueryService {

  OpenAiProperties openAiProperties;

  public void functionCall(LlmQuery llmQuery) {
    OpenAiService service = new OpenAiService(openAiProperties.getToken());
    // TODO: return the result
    ChatCompletionResult chatCompletion = service.createChatCompletion(llmQuery.getChatCompletionRequest());

    // TODO: handle incomplete response, 0 responses, not function call etc
    ChatFunctionCall functionCall = chatCompletion.getChoices()
        .get(0)
        .getMessage()
        .getFunctionCall();
    if (functionCall != null) {
      log.info("Function call: {} with args {}", functionCall.getName(), functionCall.getArguments());
      Optional<ChatMessage> message = llmQuery.getFunctionExecutor()
          .executeAndConvertToMessageSafely(functionCall);
      if (message.isEmpty()) {
        log.warn("Something went wrong with the execution of " + functionCall.getName() + "...");
      }
    }
  }
}
