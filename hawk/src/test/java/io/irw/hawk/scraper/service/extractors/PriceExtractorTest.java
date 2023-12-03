package io.irw.hawk.scraper.service.extractors;

import static io.irw.hawk.scraper.service.extractors.PriceExtractor.calculateExpectedProfitPct;
import static io.irw.hawk.scraper.service.extractors.PriceExtractor.calculateExpectedProfitUsd;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

class PriceExtractorTest {

  public static final int LISTING_PRICE_PER_PIECE_USD = 5;
  public static final int SHIPPING_PRICE_PER_PIECE_USD = 1;
  public static final int MEEST_SH_OVERHEAD = 10;
  public static final int NUMBER_OF_PIECES = 8;
  public static final int USUAL_PRICE_PER_PIECE_USD = 9;
  public static final double DELTA = 0.01;

  @Test
  void shouldCalculateExpectedProfitUsd() {
    BigDecimal calculatedProfitUsd = calculateExpectedProfitUsd(
        BigDecimal.valueOf(LISTING_PRICE_PER_PIECE_USD + SHIPPING_PRICE_PER_PIECE_USD),
        BigDecimal.valueOf(USUAL_PRICE_PER_PIECE_USD), NUMBER_OF_PIECES, BigDecimal.valueOf(MEEST_SH_OVERHEAD));
    double expectedProfitUsdValue = (USUAL_PRICE_PER_PIECE_USD * NUMBER_OF_PIECES
        - (LISTING_PRICE_PER_PIECE_USD + SHIPPING_PRICE_PER_PIECE_USD) * NUMBER_OF_PIECES) - MEEST_SH_OVERHEAD;
     assertThat(calculatedProfitUsd.doubleValue()).isEqualTo(expectedProfitUsdValue, Offset.offset(DELTA));
  }

  @Test
  void shouldCalculateExpectedProfitPct() {
    double expectedProfitUsd = USUAL_PRICE_PER_PIECE_USD * NUMBER_OF_PIECES
        - (LISTING_PRICE_PER_PIECE_USD + SHIPPING_PRICE_PER_PIECE_USD) * NUMBER_OF_PIECES - MEEST_SH_OVERHEAD;
    BigDecimal calculatedProfitPct = calculateExpectedProfitPct(BigDecimal.valueOf(expectedProfitUsd),
        BigDecimal.valueOf(LISTING_PRICE_PER_PIECE_USD * NUMBER_OF_PIECES),
        BigDecimal.valueOf(SHIPPING_PRICE_PER_PIECE_USD * NUMBER_OF_PIECES), BigDecimal.valueOf(MEEST_SH_OVERHEAD));

    double expectedProfitPercentValue = (expectedProfitUsd /
        ((LISTING_PRICE_PER_PIECE_USD + SHIPPING_PRICE_PER_PIECE_USD) * NUMBER_OF_PIECES + MEEST_SH_OVERHEAD))
        * 100;
    assertThat(calculatedProfitPct).matches(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0);
    assertThat(calculatedProfitPct.doubleValue()).isEqualTo(expectedProfitPercentValue, Offset.offset(DELTA));
  }
}