package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.mapper.EbayHighlightMapper;
import io.irw.hawk.repository.EbayHighlightRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class EbayHighlightService {

  EbayHighlightMapper ebayHighlightMapper;
  EbayHighlightRepository ebayHighlightRepository;

//  HawkScrapeRunRepository hawkFlightRepository;
//  HawkScrapeRunMapper hawkScrapeRunMapper;
//  HawkFlightService hawkFlightService;
//
//  @Transactional
//  public HawkScrapeRunDto startScrapeRun(ProductVariantEnum targetProductVariant) {
//    HawkScrapeRunDto hawkScrapeRunDto = HawkScrapeRunDto.builder()
//        .hawkFlight(hawkFlightService.getCurrentFlight())
//        .startedAt(Instant.now())
//        .productVariant(targetProductVariant)
//        .build();
//    hawkFlightRepository.save(hawkScrapeRunMapper.toEntity(hawkScrapeRunDto));
//    return hawkScrapeRunDto;
//  }


}
