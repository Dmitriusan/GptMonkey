package io.irw.hawk.scraper.service.openai;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openai")
@Value
public class OpenAiProperties {

  private String token;

}
