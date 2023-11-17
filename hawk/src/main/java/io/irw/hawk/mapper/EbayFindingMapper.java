package io.irw.hawk.mapper;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.entity.EbayFinding;
import java.math.BigDecimal;
import java.util.Optional;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = ConfigMapper.class)
public interface EbayFindingMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ebayIdStr", ignore = true)
  EbayFinding updateFindingEntity(@MappingTarget EbayFinding persistentFindingEntity, EbayFindingDto latestFindingDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ebayIdStr", source = "itemSummary.itemId")
  @Mapping(target = "legacyEbayIdStr", source = "itemSummary.legacyItemId")
  @Mapping(target = "seller", source = "ebaySellerDto")
  @Mapping(target = "title", source = "itemSummary.title")
  @Mapping(target = "listingCreatedAt", source = "itemSummary.itemCreationDate")
  @Mapping(target = "webUrl", source = "itemSummary.itemWebUrl")
  @Mapping(target = "topRatedBuyingExperience", source = "itemSummary.topRatedBuyingExperience")
  EbayFindingDto itemSummaryToEbayFindingDto(ItemSummary itemSummary, EbaySellerDto ebaySellerDto);


  // Add the following mappings to handle the new fields in the EbayFindingDto
  @Mapping(target = "numberOfPieces", expression = "java(mapOptionalInteger(ebayFindingDto.getNumberOfPieces()))")
  @Mapping(target = "bidCount", expression = "java(mapOptionalInteger(ebayFindingDto.getBidCount()))")
  @Mapping(target = "itemDescription", expression = "java(mapOptionalString(ebayFindingDto.getItemDescription()))")
  @Mapping(target = "currentAuctionPriceUsd", expression = "java(mapOptionalBigDecimal(ebayFindingDto.getCurrentAuctionPriceUsd()))")
  @Mapping(target = "buyItNowPriceUsd", expression = "java(mapOptionalBigDecimal(ebayFindingDto.getBuyItNowPriceUsd()))")
  @Mapping(target = "minShippingCostUsd", expression = "java(mapOptionalBigDecimal(ebayFindingDto.getMinShippingCostUsd()))")
  @Mapping(target = "buyNowPricePerPieceWithShippingUsd", expression = "java(mapOptionalBigDecimal(ebayFindingDto.getBuyNowPricePerPieceWithShippingUsd()))")
  @Mapping(target = "currentAucPricePerPieceWithShippingUsd", expression = "java(mapOptionalBigDecimal(ebayFindingDto.getCurrentAucPricePerPieceWithShippingUsd()))")
  @Mapping(target = "finalAuctionPriceWithoutShippingUsd", expression = "java(mapOptionalBigDecimal(ebayFindingDto.getFinalAuctionPriceWithoutShippingUsd()))")
  EbayFinding toEntity(EbayFindingDto ebayFindingDto);


  // Method to map from EbayFinding entity to EbayFindingDto
  @Mapping(target = "itemDescription", expression = "java(mapToOptionalString(ebayFinding.getItemDescription()))")
  @Mapping(target = "numberOfPieces", expression = "java(mapToOptionalInteger(ebayFinding.getNumberOfPieces()))")
  @Mapping(target = "bidCount", expression = "java(mapToOptionalInteger(ebayFinding.getBidCount()))")
  @Mapping(target = "currentAuctionPriceUsd", expression = "java(mapToOptionalBigDecimal(ebayFinding.getCurrentAuctionPriceUsd()))")
  @Mapping(target = "buyItNowPriceUsd", expression = "java(mapToOptionalBigDecimal(ebayFinding.getBuyItNowPriceUsd()))")
  @Mapping(target = "minShippingCostUsd", expression = "java(mapToOptionalBigDecimal(ebayFinding.getMinShippingCostUsd()))")
  @Mapping(target = "buyNowPricePerPieceWithShippingUsd", expression = "java(mapToOptionalBigDecimal(ebayFinding.getBuyNowPricePerPieceWithShippingUsd()))")
  @Mapping(target = "currentAucPricePerPieceWithShippingUsd", expression = "java(mapToOptionalBigDecimal(ebayFinding.getCurrentAucPricePerPieceWithShippingUsd()))")
  @Mapping(target = "finalAuctionPriceWithoutShippingUsd", expression = "java(mapToOptionalBigDecimal(ebayFinding.getFinalAuctionPriceWithoutShippingUsd()))")
  EbayFindingDto toDto(EbayFinding ebayFinding);

  // Handle mapping Optional fields to nullable columns in the entity
  default Integer mapOptionalInteger(Optional<Integer> value) {
    if (value != null) {
      return value.orElse(null);
    } else {
      return null;
    }
  }

  default BigDecimal mapOptionalBigDecimal(Optional<BigDecimal> value) {
    if (value != null) {
      return value.orElse(null);
    } else {
      return null;
    }
  }

  default String mapOptionalString(Optional<String> value) {
    if (value != null) {
      return value.orElse(null);
    } else {
      return null;
    }
  }

  // Helper methods to map null values to Optional
  default Optional<String> mapToOptionalString(String value) {
    return Optional.ofNullable(value);
  }

  default Optional<Integer> mapToOptionalInteger(Integer value) {
    return Optional.ofNullable(value);
  }

  default Optional<BigDecimal> mapToOptionalBigDecimal(BigDecimal value) {
    return Optional.ofNullable(value);
  }

}