package io.irw.hawk.scraper.service.domain;

import static java.util.stream.Collectors.toMap;

import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.dto.merchandise.MerchandiseVerdictType;
import io.irw.hawk.entity.EbayHighlight;
import io.irw.hawk.mapper.EbayHighlightMapper;
import io.irw.hawk.repository.EbayHighlightRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
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

  @Transactional(readOnly = true)
  public ScrapeRunSummaryDto getScrapeRunSummary(HawkScrapeRunDto runDto) {
    List<EbayHighlight> highlights = ebayHighlightRepository.findByRun_Id(runDto.getId());

    Map<MerchandiseVerdictType, Integer> verdictCounts = highlights.stream()
        .collect(toMap(EbayHighlight::getAggregatedVerdict, hl -> 1, Integer::sum));

    return ScrapeRunSummaryDto.builder()
        .totalHighligts(highlights.size())
        .verdictCounts(verdictCounts)
        .build();
  }

  @Transactional(readOnly = true)
  public Pair<List<EbayHighlightDto>, List<EbayHighlightDto>> getTopHighlights(HawkScrapeRunDto runDto) {
    Set<Long> highlightIds = new HashSet<>();
    List<EbayHighlightDto> topBuyNowHiglights = ebayHighlightRepository.findTopBuyNowHighlightsByRunId(runDto.getId())
        .stream()
        .map(hl -> ebayHighlightMapper.toDto(hl))
        .filter(hl -> hl.getAggregatedVerdict().isBuyable())
        .map(hl -> highlightIds.add(hl.getId()) ? hl : null)
        .toList();

    List<EbayHighlightDto> topAucHiglights = ebayHighlightRepository.findTopAucHighlightsByRunId(runDto.getId())
        .stream()
        .map(hl -> ebayHighlightMapper.toDto(hl))
        .filter(hl -> hl.getAggregatedVerdict().isBuyable())
        .filter(hl -> ! highlightIds.contains(hl.getId()))
        .toList();

    return Pair.of(topBuyNowHiglights, topAucHiglights);
  }

  @Value
  @Builder
  public static class ScrapeRunSummaryDto {

    int totalHighligts;
    Map<MerchandiseVerdictType, Integer> verdictCounts;

  }

}
