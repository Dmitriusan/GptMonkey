package io.irw.hawk.mapper;

import com.ebay.buy.browse.model.Seller;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.entity.EbaySeller;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = ConfigMapper.class)
public interface EbaySellerMapper {

  EbaySeller toEntity(EbaySellerDto ebaySellerDto);

  EbaySellerDto toDto(EbaySeller ebaySeller);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  EbaySeller partialUpdate(
      EbaySellerDto ebaySellerDto, @MappingTarget EbaySeller ebaySeller);

  @Mapping(target = "reputationPercentage", source = "feedbackPercentage", qualifiedByName = "stringToFloat")
  @Mapping(target = "feedbackScore", source = "feedbackScore")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ebayIdStr", source = "username")
  @Mapping(target = "registeredOn", ignore = true) // TODO: set from seller info
  EbaySellerDto sellerToEbaySellerDto(Seller seller);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ebayIdStr", ignore = true)
  @Mapping(target = "registeredOn", ignore = true)
  EbaySeller updateSellerEntity(@MappingTarget EbaySeller persistentSellerEntity, EbaySellerDto latestSellerDto);

  @Named("stringToFloat")
  default float stringToFloat(String value) {
    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException e) {
      return 0.0f; // or some default value
    }
  }
}
