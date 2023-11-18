package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.entity.EbayFinding;
import io.irw.hawk.mapper.EbayFindingMapper;
import io.irw.hawk.repository.EbayFindingRepository;
import jakarta.persistence.EntityNotFoundException;
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
  public EbayFindingDto updateFinding(EbayFindingDto ebayFindingDto) {
    Optional<EbayFinding> optionalExistingFinding = ebayFindingRepository.findByEbayIdStr(ebayFindingDto.getEbayIdStr());
    if (optionalExistingFinding.isPresent()) {
      EbayFinding existingFinding = optionalExistingFinding.get();
      ebayFindingMapper.updateFindingEntity(existingFinding, ebayFindingDto);
      EbayFinding updatedFinding = ebayFindingRepository.save(existingFinding);
      return ebayFindingMapper.toDto(updatedFinding);
    } else {
      throw new EntityNotFoundException("EbayFinding with eBay ID " + ebayFindingDto.getEbayIdStr() + " not found.");
    }
  }
}
