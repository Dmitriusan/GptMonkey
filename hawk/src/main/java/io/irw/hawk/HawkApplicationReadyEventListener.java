package io.irw.hawk;

import io.irw.hawk.scraper.service.domain.HawkFlightService;
import io.irw.hawk.scraper.service.scrape.ScraperService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class HawkApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

  ScraperService scraperService;
  HawkFlightService hawkFlightService;


  @Override
  @SneakyThrows
  public void  onApplicationEvent(ApplicationReadyEvent event) {
    log.info("Running Hawk service");
    hawkFlightService.startFlight();
    scraperService.scrape();
    hawkFlightService.finishFlight();
  }

}
