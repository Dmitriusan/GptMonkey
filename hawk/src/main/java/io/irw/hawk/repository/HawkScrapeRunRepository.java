package io.irw.hawk.repository;

import io.irw.hawk.entity.HawkScrapeRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HawkScrapeRunRepository extends JpaRepository<HawkScrapeRun, Long> {

}