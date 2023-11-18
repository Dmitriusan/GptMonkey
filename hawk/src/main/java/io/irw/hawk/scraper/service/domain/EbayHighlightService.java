package io.irw.hawk.scraper.service.domain;

import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.entity.EbayHighlight;
import io.irw.hawk.mapper.EbayHighlightMapper;
import io.irw.hawk.repository.EbayHighlightRepository;
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
public class EbayHighlightService {

  EbayHighlightMapper ebayHighlightMapper;
  EbayHighlightRepository ebayHighlightRepository;

  @Transactional
  public EbayHighlightDto saveHighlight(EbayHighlightDto ebayHighlightDto) {
    EbayHighlight persisted = ebayHighlightRepository.save(ebayHighlightMapper.toEntity(ebayHighlightDto));
    return ebayHighlightMapper.toDto(persisted);
  }

}
