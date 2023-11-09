package io.irw.hawk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchandise_item")
public class Item {

  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "ebay_id")
  private String ebayId;

  @Column(name = "ebay_legacy_id")
  private String ebayLegacyId;

  @Column(name = "item_name")
  private String itemName;

  @Column(name = "price_usd", nullable = false)
  private float priceUsd;

}