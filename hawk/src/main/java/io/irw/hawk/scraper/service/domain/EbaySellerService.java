package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.entity.EbaySeller;
import io.irw.hawk.mapper.EbaySellerMapper;
import io.irw.hawk.repository.EbaySellerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class EbaySellerService {

  EbaySellerMapper ebaySellerMapper;
  EbaySellerRepository ebaySellerRepository;

  @Transactional
  public EbaySellerDto saveSeller(EbaySellerDto ebaySellerDto) {
    EbaySeller persisted = ebaySellerRepository.save(ebaySellerMapper.toEntity(ebaySellerDto));
    return ebaySellerMapper.toDto(persisted);
  }

  @Transactional
  public EbaySellerDto upsertSeller(EbaySellerDto ebaySellerDto) {
    return ebaySellerRepository.findByEbayIdStr(ebaySellerDto.getEbayIdStr())
        .map(existingSeller -> {
          EbaySeller updatedSeller = ebaySellerRepository.save(
              ebaySellerMapper.updateSellerEntity(existingSeller,ebaySellerDto));
          return ebaySellerMapper.toDto(updatedSeller);
        })
        .orElseGet(() -> saveSeller(ebaySellerDto));
  }

}
