package io.irw.hawk.scraper.model;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MerchandiseVerdictType {

  /**
   * Can not process data
   */
  UNPROCESSABLE,

  /**
   * Hard reject - wrong match, or issues like reputation
   */
  REJECT,

  /**
   * Soft reject - not interesting from business point of view (e.g. the price is too high)
   */
  NOT_INTERESTING,

  /**
   * Human intervention required - not sure about interest
   */
  HUMAN_INTERVENTION_REQUIRED,

  /**
   * Good match
   */
  BUY;

  private static final List<MerchandiseVerdictType> BUYABLE_TYPES = Arrays.asList(BUY, HUMAN_INTERVENTION_REQUIRED);

  public boolean isBuyable() {
    return BUYABLE_TYPES.contains(this);
  }

}
