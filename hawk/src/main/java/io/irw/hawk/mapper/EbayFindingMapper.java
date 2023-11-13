package io.irw.hawk.mapper;

import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.entity.EbayFinding;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = ConfigMapper.class)
public interface EbayFindingMapper {

  EbayFinding toEntity(EbayFindingDto ebayFindingDto);

  EbayFindingDto toDto(EbayFinding ebayFinding);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  EbayFinding partialUpdate(
      EbayFindingDto ebayFindingDto, @MappingTarget EbayFinding ebayFinding);

}
