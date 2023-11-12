package io.irw.hawk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Items that were found and not filtered out during the scrape run. There was some reasoning and resolution about
 * these items.
 */
@Getter
@Setter
@Entity
@Table(name = "ebay_highlight", schema = "merchandise_db")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EbayHighlight {

  @Id
  @Column(name = "id", updatable = false)
  @SequenceGenerator(name = "ebay_highlight_seq",
      sequenceName = "ebay_highlight_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "ebay_highlight_seq")
  Long id;

  @ManyToOne
  @JoinColumn(name = "run_id")
  @EqualsAndHashCode.Include
  HawkScrapeRun runId;

  @ManyToOne
  @JoinColumn(name = "ebay_finding_id")
  @EqualsAndHashCode.Include
  EbayFinding ebayFinding;

  



}
