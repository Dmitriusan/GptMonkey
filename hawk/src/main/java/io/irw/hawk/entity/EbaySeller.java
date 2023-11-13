package io.irw.hawk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Some info collected about the seller
 */
@Getter
@Setter
@Entity
@Table(name = "ebay_seller", schema = "merchandise_db")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EbaySeller {

  @Id
  @Column(name = "id", updatable = false)
  @SequenceGenerator(name = "ebay_seller_seq",
      sequenceName = "ebay_seller_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "ebay_seller_seq")
  Long id;

  @Column(name = "ebay_id_str")
  @EqualsAndHashCode.Include
  String ebayIdStr;

  @Column(name = "registered_on")
  Instant registeredOn;

  @Column(name = "reputation_percentage")
  float reputationPercentage;

  @Column(name = "feedback_score")
  int feedbackScore;

}
