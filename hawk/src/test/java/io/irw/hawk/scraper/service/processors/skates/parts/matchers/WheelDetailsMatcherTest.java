package io.irw.hawk.scraper.service.processors.skates.parts.matchers;

import static io.irw.hawk.scraper.service.processors.skates.parts.matchers.WheelDetailsMatcher.checkForMicrobearings;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WheelDetailsMatcherTest {

  @Test
  void shouldDetectMicrobearingWheels() {
    assertThat(checkForMicrobearings(
        "Labeda Single Wheel RPG X-Soft RED Inline Indoor Hockey~72MM~76MM~80MM~~~~ 74A"
    )).isFalse();

    assertThat(checkForMicrobearings(
        "Set of 8 Labeda Fuzion 80mm X-Soft Inline Skate Wheels Micro Bearing"
    )).isTrue();

    assertThat(checkForMicrobearings(
        "set of 8 labeda fuzion 80mm x-soft inline skate wheels they take micro bearings(not included)."
            + " labeda fuzion wheels."
    )).isTrue();
  }
}