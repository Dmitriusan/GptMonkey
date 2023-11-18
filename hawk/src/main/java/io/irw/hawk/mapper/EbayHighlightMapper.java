package io.irw.hawk.mapper;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.entity.EbayHighlight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = ConfigMapper.class, uses = {HawkScrapeRunMapper.class, EbayFindingMapper.class})
public interface EbayHighlightMapper {

//    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
//      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "run", source = "hawkScrapeRunDto")
  @Mapping(target = "ebayFinding", source = "ebayFindingDto")
  @Mapping(target = "finalVerdict", source = "ebayHighlightDto.finalVerdict")
  EbayHighlightDto fromRuntime(HawkScrapeRunDto hawkScrapeRunDto,
      EbayFindingDto ebayFindingDto, EbayHighlightDto ebayHighlightDto);

  EbayHighlight toEntity(EbayHighlightDto ebayHighlightDto);

  EbayHighlightDto toDto(EbayHighlight ebayHighlight);

}
