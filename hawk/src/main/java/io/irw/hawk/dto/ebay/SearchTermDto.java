package io.irw.hawk.dto.ebay;

import io.irw.hawk.integration.ebay.buy.browse.ItemSummarySearchParameterDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchTermDto {

  long id;
  ItemSummarySearchParameterDto searchParams;
//  String name;
//  String query;

}
