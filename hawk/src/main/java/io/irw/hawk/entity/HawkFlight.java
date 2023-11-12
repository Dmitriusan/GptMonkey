package io.irw.hawk.entity;

import io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStateEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "hawk_flight", schema = "merchandise_db")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HawkFlight {

  @Id
  @Column(name = "id", updatable = false)
  @SequenceGenerator(name = "hawk_flight_seq",
      sequenceName = "hawk_flight_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "hawk_flight_seq")
  Long id;

  @Column(name = "started_at", nullable = false, updatable = false)
  @EqualsAndHashCode.Include
  Instant startedAt;

  @Column(name = "state", nullable = false)
  HawkFlightStateEnum state;

  @Column(name = "ended_at")
  Instant endedAt;

}