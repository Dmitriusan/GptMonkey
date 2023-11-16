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
   * Item is already present at the DB
   */
  ITEM_ALREADY_PERSISTED,

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
   * Good match to BUY_IT_NOW, but needs a manual review
   */
  BUYING_OPPORTUNITY,

  /**
   * Good match for auto sniping, but needs manual review
   */
  SNIPING_OPPORTUNITY,

  /**
   * Definitely good match to BUY_IT_NOW
   */
  BUY_IT_NOW_RECOMMENDED,

  /**
   * Definitely good match, auto-add to sniping list
   */
  SNIPE_RECOMMENDED;

  private static final List<MerchandiseVerdictType> BUYABLE_TYPES = Arrays.asList(
      SNIPE_RECOMMENDED, BUY_IT_NOW_RECOMMENDED,
      BUYING_OPPORTUNITY, SNIPING_OPPORTUNITY,
      HUMAN_INTERVENTION_REQUIRED);

  public boolean isBuyable() {
    return BUYABLE_TYPES.contains(this);
  }

}
