package io.irw.hawk.mapper;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.entity.EbayHighlight;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ConfigMapper.class, uses = {HawkScrapeRunMapper.class, EbayFindingMapper.class})
public interface EbayHighlightMapper {

//    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
//      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//  @Mapping(target = "id", ignore = true)
//  @Mapping(target = "run", source = "hawkScrapeRunDto")
//  @Mapping(target = "ebayFinding", source = "ebayFindingDto")
//  @Mapping(target = "aggregatedVerdict", source = "ebayHighlightDto.aggregatedVerdict")
//  EbayHighlightDto fromRuntime(HawkScrapeRunDto hawkScrapeRunDto,
//      EbayFindingDto ebayFindingDto, EbayHighlightDto ebayHighlightDto);

  @Mapping(target = "reasoningSummary", expression = "java(getReasoningSummary(ebayHighlightDto))")
  EbayHighlight toEntity(EbayHighlightDto ebayHighlightDto);

  @Mapping(target = "newItem", expression = "java(false)")
  EbayHighlightDto toDto(EbayHighlight ebayHighlight);

  default String getReasoningSummary(EbayHighlightDto ebayHighlightDto) {
    return ebayHighlightDto.getPipelineMetadata()
        .filterReasoningsFromLog()
        .stream()
        .map(merchandiseReasoningLog ->
            "%s %s".formatted(merchandiseReasoningLog.getVerdict(), merchandiseReasoningLog.getReason())
        )
        .collect(Collectors.joining("\n"));
  }
}
