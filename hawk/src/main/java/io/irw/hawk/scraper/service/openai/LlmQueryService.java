package io.irw.hawk.scraper.service.openai;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.irw.hawk.scraper.ai.LlmQuery;
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

  public void chatCompletion(LlmQuery llmQuery) {
    OpenAiService service = new OpenAiService(openAiProperties.getToken());
    CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt(llmQuery.getPrompt())
        .model(llmQuery.getModel().getName())
        .echo(false)
        .build();

    // TODO: return the result
    service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
  }

  public void function(LlmQuery llmQuery) {
    OpenAiService service = new OpenAiService(openAiProperties.getToken());
    ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
        .messages(llmQuery.getPrompt())
        .model(llmQuery.getModel().getName())
        .echo(false)
        .build();
    service.createChatCompletion(completionRequest).getChoices().forEach(System.out::println);
    // TODO: return the result
  }

}
