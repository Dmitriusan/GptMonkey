package io.irw.hawk.repository;

import io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStateEnum;
import io.irw.hawk.entity.HawkFlight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface HawkFlightRepository extends JpaRepository<HawkFlight, Long>, JpaSpecificationExecutor<HawkFlight> {

  @Query("select h from HawkFlight h where h.state = ?1")
  HawkFlight findByState(HawkFlightStateEnum state);

  HawkFlight findOneByState(HawkFlightStateEnum state);

  List<HawkFlight> findAllByEndedAtNull();

  HawkFlight findOneByEndedAtNull();

}