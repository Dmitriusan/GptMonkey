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
 * A small and partial reflection of eBay listings that has been processed by the app. These listings can be found
 * during multiple scrape runs, so this table may be updated multiple times.
 */
@Getter
@Setter
@Entity
@Table(name = "ebay_finding", schema = "merchandise_db")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EbayFinding {

  @Id
  @Column(name = "id", updatable = false)
  @SequenceGenerator(name = "ebay_finding_seq",
      sequenceName = "ebay_finding_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "ebay_finding_seq")
  Long id;




}
