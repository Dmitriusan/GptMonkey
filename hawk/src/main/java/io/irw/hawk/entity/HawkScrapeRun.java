package io.irw.hawk.entity;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.repository.converters.ProductVariantEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "hawk_scrape_run", schema = "merchandise_db")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HawkScrapeRun {

  @Id
  @Column(name = "id", updatable = false)
  @SequenceGenerator(name = "hawk_scrape_run_seq",
      sequenceName = "hawk_scrape_run_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "hawk_scrape_run_seq")
  Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "flight_id", nullable=false, updatable=false)
  HawkFlight hawkFlight;

  @EqualsAndHashCode.Include
  @Column(name = "started_at", updatable = false)
  Instant startedAt;

  @EqualsAndHashCode.Include
  @Column(name = "product_variant", updatable = false)
  @Convert(converter = ProductVariantEnumConverter.class)
  ProductVariantEnum productVariant;

}