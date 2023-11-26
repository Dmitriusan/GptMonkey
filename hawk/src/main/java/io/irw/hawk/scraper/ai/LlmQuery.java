package io.irw.hawk.scraper.ai;

import com.theokanning.openai.completion.chat.ChatMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class LlmQuery {

  List<ChatMessage> messages;

  @Default
  OpenAiModel model = OpenAiModel.GPT_3_5_TURBO;

  @Getter
  @AllArgsConstructor
  public enum OpenAiModel {
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
    GPT_4_TURBO("gpt-4-1106-preview");

    private String name;

  }

}
