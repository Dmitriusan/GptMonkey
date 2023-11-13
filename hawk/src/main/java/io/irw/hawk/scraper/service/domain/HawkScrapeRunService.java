package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.mapper.HawkScrapeRunMapper;
import io.irw.hawk.repository.HawkScrapeRunRepository;
import io.irw.hawk.scraper.service.domain.HawkFlightService;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class HawkScrapeRunService {

  HawkScrapeRunRepository hawkFlightRepository;
  HawkScrapeRunMapper hawkScrapeRunMapper;
  HawkFlightService hawkFlightService;

  @Transactional
  public HawkScrapeRunDto startScrapeRun(ProductVariantEnum targetProductVariant) {
    HawkScrapeRunDto hawkScrapeRunDto = HawkScrapeRunDto.builder()
        .hawkFlight(hawkFlightService.getCurrentFlight())
        .startedAt(Instant.now())
        .productVariant(targetProductVariant)
        .build();
    hawkFlightRepository.save(hawkScrapeRunMapper.toEntity(hawkScrapeRunDto));
    return hawkScrapeRunDto;
  }


}
