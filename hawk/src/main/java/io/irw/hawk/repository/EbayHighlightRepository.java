package io.irw.hawk.repository;

import io.irw.hawk.entity.EbayHighlight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EbayHighlightRepository extends JpaRepository<EbayHighlight, Long> {

  List<EbayHighlight> findByRun_Id(Long id);

  @Query(value = "SELECT eh.* FROM ebay_highlight eh " +
      "JOIN ebay_finding ef on eh.ebay_finding_id = ef.id " +
      "WHERE eh.run_id = ?1 " +
      "AND 'FIXED_PRICE' = ANY(ef.buying_options) " +
      "AND eh.expected_buy_now_profit_usd > 0 " +
      "ORDER BY eh.expected_buy_now_profit_usd DESC, eh.expected_buy_now_profit_pct DESC",
      nativeQuery = true)
  List<EbayHighlight> findTopBuyNowHighlightsByRunId(Long scrapeRunId);

  @Query(value = "SELECT eh.* FROM ebay_highlight eh " +
      "JOIN ebay_finding ef on eh.ebay_finding_id = ef.id " +
      "WHERE eh.run_id = ?1 " +
      "AND 'AUCTION' = ANY(ef.buying_options) " +
      "AND eh.possible_auction_profit_pct > 0 " +
      "ORDER BY eh.possible_auction_profit_usd DESC, eh.possible_auction_profit_pct DESC",
      nativeQuery = true)
  List<EbayHighlight> findTopAucHighlightsByRunId(Long scrapeRunId);

}