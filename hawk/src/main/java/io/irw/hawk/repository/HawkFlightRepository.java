package io.irw.hawk.repository;

import io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStatusEnum;
import io.irw.hawk.entity.HawkFlight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface HawkFlightRepository extends JpaRepository<HawkFlight, Long>, JpaSpecificationExecutor<HawkFlight> {

  HawkFlight findOneByStatus(HawkFlightStatusEnum state);

  List<HawkFlight> findAllByEndedAtNull();

}