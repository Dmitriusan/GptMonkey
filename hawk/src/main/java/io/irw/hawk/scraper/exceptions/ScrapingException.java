package io.irw.hawk.scraper.exceptions;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ScrapingException extends RuntimeException {
  public ScrapingException(String message) {
    super(message);
  }

  public ScrapingException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScrapingException(Throwable cause) {
    super(cause);
  }

  public ScrapingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
