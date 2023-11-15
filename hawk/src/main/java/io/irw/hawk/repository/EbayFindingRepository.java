package io.irw.hawk.repository;

import io.irw.hawk.entity.EbayFinding;
import io.irw.hawk.entity.HawkScrapeRun;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EbayFindingRepository extends JpaRepository<EbayFinding, Long> {

  Optional<EbayFinding> findByEbayIdStr(String ebayIdStr);

}