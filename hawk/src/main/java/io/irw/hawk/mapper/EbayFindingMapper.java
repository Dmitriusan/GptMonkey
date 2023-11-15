package io.irw.hawk.mapper;

import com.ebay.buy.browse.model.ItemSummary;
import com.ebay.buy.browse.model.Seller;
import io.irw.hawk.dto.ebay.EbayFindingDto;
import io.irw.hawk.dto.ebay.EbaySellerDto;
import io.irw.hawk.entity.EbayFinding;
import io.irw.hawk.entity.EbaySeller;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = ConfigMapper.class)
public interface EbayFindingMapper {

  EbayFinding toEntity(EbayFindingDto ebayFindingDto);

  EbayFindingDto toDto(EbayFinding ebayFinding);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  EbayFinding partialUpdate(
      EbayFindingDto ebayFindingDto, @MappingTarget EbayFinding ebayFinding);


  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ebayIdStr", ignore = true)
  EbayFinding updateFindingEntity(@MappingTarget EbayFinding persistentFindingEntity, EbayFindingDto latestFindingDto);

//  @Mapping(target = "id", ignore = true)
//  @Mapping(target = "ebayIdStr", source = "itemId")
//  @Mapping(target = "legacyEbayIdStr", source = "legacyItemId")
//  EbayFindingDto itemSummaryToEbayFindingDto(ItemSummary itemSummary);

}
