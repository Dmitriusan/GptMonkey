package io.irw.hawk.scraper.service.domain;

import static io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStatusEnum.FAILED;
import static io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStatusEnum.IN_PROGRESS;

import io.irw.hawk.dto.merchandise.HawkFlightDto;
import io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStatusEnum;
import io.irw.hawk.entity.HawkFlight;
import io.irw.hawk.mapper.HawkFlightMapper;
import io.irw.hawk.repository.HawkFlightRepository;
import java.time.Instant;
import java.util.List;
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
public class HawkFlightService {

  HawkFlightRepository hawkFlightRepository;
  HawkFlightMapper hawkFlightMapper;

  @Transactional
  public void startFlight() {
    log.info("Starting Hawk flight");
    // Close previous flights with endedOn = null
    forceClosePreviousUnfinishedFlights();

    HawkFlightDto hawkFlightDto = HawkFlightDto.builder()
        .status(IN_PROGRESS)
        .startedAt(Instant.now())
        .build();
    hawkFlightRepository.save(hawkFlightMapper.toEntity(hawkFlightDto));
  }

  private void forceClosePreviousUnfinishedFlights() {
    List<HawkFlight> allUnfinishedPreviousFlights = hawkFlightRepository.findAllByEndedAtNull();

    if (! allUnfinishedPreviousFlights.isEmpty()) {
      log.warn("Force finishing {} unfinished flights", allUnfinishedPreviousFlights.size());
    }

    allUnfinishedPreviousFlights.forEach(flight -> {
      flight.setStatus(FAILED);
      flight.setEndedAt(Instant.now());
    });
    hawkFlightRepository.saveAll(allUnfinishedPreviousFlights);
  }

  @Transactional(readOnly = true)
  public HawkFlightDto getCurrentFlight() {
    return hawkFlightMapper.toDto(hawkFlightRepository.findOneByStatus(IN_PROGRESS));
  }

  @Transactional
  public void finishFlight() {
    HawkFlight inProgressFlight = hawkFlightRepository.findOneByStatus(IN_PROGRESS);
    inProgressFlight.setStatus(HawkFlightStatusEnum.ENDED);
    inProgressFlight.setEndedAt(Instant.now());
    hawkFlightRepository.save(inProgressFlight);
    log.info("Finishing Hawk flight");
  }
}
