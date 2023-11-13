package io.irw.hawk.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import io.irw.hawk.dto.merchandise.HawkFlightDto.HawkFlightStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

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

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, columnDefinition = "hawk_flight_status")
  @Type(PostgreSQLEnumType.class)
  HawkFlightStatusEnum status;

  @Column(name = "ended_at")
  Instant endedAt;

}