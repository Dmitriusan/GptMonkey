package io.irw.hawk.repository;

import io.irw.hawk.entity.EbaySeller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EbaySellerRepository extends JpaRepository<EbaySeller, Long> {

}