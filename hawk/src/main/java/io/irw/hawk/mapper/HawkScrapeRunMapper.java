package io.irw.hawk.mapper;

import io.irw.hawk.dto.merchandise.HawkScrapeRunDto;
import io.irw.hawk.entity.HawkScrapeRun;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = ConfigMapper.class)
public interface HawkScrapeRunMapper {

  HawkScrapeRun toEntity(HawkScrapeRunDto hawkScrapeRunDto);

  HawkScrapeRunDto toDto(HawkScrapeRun hawkScrapeRun);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  HawkScrapeRun partialUpdate(
      HawkScrapeRunDto hawkScrapeRunDto, @MappingTarget HawkScrapeRun hawkScrapeRun);
}