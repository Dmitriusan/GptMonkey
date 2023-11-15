package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.entity.EbayFinding;
import io.irw.hawk.entity.EbaySeller;
import io.irw.hawk.mapper.EbayFindingMapper;
import io.irw.hawk.mapper.HawkScrapeRunMapper;
import io.irw.hawk.repository.EbayFindingRepository;
import io.irw.hawk.repository.HawkScrapeRunRepository;
import java.time.Instant;
import java.util.Optional;
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
public class EbayFindingService {

  EbayFindingMapper ebayFindingMapper;
  EbayFindingRepository ebayFindingRepository;

  @Transactional
  public EbayFindingDto saveFinding(EbayFindingDto ebayFindingDto) {
    EbayFinding persisted = ebayFindingRepository.save(ebayFindingMapper.toEntity(ebayFindingDto));
    return ebayFindingMapper.toDto(persisted);
  }

  public Optional<EbayFindingDto> findByEbayId(String ebayItemIdStr) {
    return ebayFindingRepository.findByEbayIdStr(ebayItemIdStr)
        .map(ebayFindingMapper::toDto);
  }

  @Transactional
  public EbayFindingDto upsertFinding(EbayFindingDto ebayFindingDto) {
    return ebayFindingRepository.findByEbayIdStr(ebayFindingDto.getEbayIdStr())
        .map(existingFinding -> {
          EbayFinding updatedFinding = ebayFindingRepository.save(
              ebayFindingMapper.updateFindingEntity(existingFinding, ebayFindingDto));
          return ebayFindingMapper.toDto(updatedFinding);
        })
        .orElseGet(() -> saveFinding(ebayFindingDto));
  }


}
