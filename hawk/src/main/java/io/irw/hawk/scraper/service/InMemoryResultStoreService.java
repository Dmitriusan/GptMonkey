package io.irw.hawk.scraper.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InMemoryResultStoreService {

  public void storeScrapingRun() {
    log.info("Storing result");
  }

  public void storeSeenItem() {
    log.info("Storing result");
  }

  public void storeFinding() {
    log.info("Storing result");
  }

}
