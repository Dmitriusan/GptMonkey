package io.irw.hawk.dto.merchandise;

import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HawkFlightDto implements Serializable {

  Long id;
  HawkFlightStatusEnum status;
  Instant startedAt;
  Instant endedAt;

  @Getter
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public enum HawkFlightStatusEnum {
    IN_PROGRESS, FAILED, ENDED;
  }
}