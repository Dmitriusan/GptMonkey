package io.irw.hawk.mapper;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbayHighlightDto;
import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.entity.EbayHighlight;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = ConfigMapper.class, uses = {HawkScrapeRunMapper.class, EbayFindingMapper.class})
public interface EbayHighlightMapper {

//    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
//      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "run", source = "hawkScrapeRunDto"),
      @Mapping(target = "ebayFinding", source = "ebayFindingDto"),
      @Mapping(target = "finalVerdict", source = "merchandiseMetadataDto.finalVerdict")
  })
  EbayHighlightDto fromRuntime(HawkScrapeRunDto hawkScrapeRunDto,
      EbayFindingDto ebayFindingDto, MerchandiseMetadataDto merchandiseMetadataDto);


  EbayHighlight toEntity(EbayHighlightDto ebayHighlightDto);

  EbayHighlightDto toDto(EbayHighlight ebayHighlight);

}
