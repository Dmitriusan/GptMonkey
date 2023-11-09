package io.irw.hawk.scraper.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ScrapeSearchException extends ScrapingException {

  public ScrapeSearchException(String message) {
    super(message);
  }

  public ScrapeSearchException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScrapeSearchException(Throwable cause) {
    super(cause);
  }

  public ScrapeSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
