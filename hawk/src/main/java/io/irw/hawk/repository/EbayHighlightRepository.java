package io.irw.hawk.repository;

import io.irw.hawk.entity.EbayFinding;
import io.irw.hawk.entity.EbayHighlight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EbayHighlightRepository extends JpaRepository<EbayHighlight, Long> {

}