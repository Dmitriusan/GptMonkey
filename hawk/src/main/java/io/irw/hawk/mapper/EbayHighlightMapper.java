package io.irw.hawk.mapper;

import io.irw.hawk.entity.EbayFinding;
import io.irw.hawk.entity.EbayHighlight;
import io.irw.hawk.entity.HawkScrapeRun;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = ConfigMapper.class)
public interface EbayHighlightMapper {

//  @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
//      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "run", source = "hawkScrapeRun"),
      @Mapping(target = "ebayFinding", source = "ebayFinding"),
      @Mapping(target = "finalVerdict", source = "merchandiseMetadataDto.finalVerdict"),
  })
  EbayHighlight toEntity(MerchandiseMetadataDto merchandiseMetadataDto, HawkScrapeRun hawkScrapeRun,
      EbayFinding ebayFinding);


}
