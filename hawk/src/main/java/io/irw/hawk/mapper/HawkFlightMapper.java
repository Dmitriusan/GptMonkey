package io.irw.hawk.mapper;

import io.irw.hawk.dto.merchandise.HawkFlightDto;
import io.irw.hawk.entity.HawkFlight;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = ConfigMapper.class)
public interface HawkFlightMapper {

  HawkFlight toEntity(HawkFlightDto hawkFlightDto);

  HawkFlightDto toDto(HawkFlight hawkFlight);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  HawkFlight partialUpdate(
      HawkFlightDto hawkFlightDto, @MappingTarget HawkFlight hawkFlight);
}