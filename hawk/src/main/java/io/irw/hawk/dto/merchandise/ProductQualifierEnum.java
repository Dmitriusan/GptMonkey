package io.irw.hawk.dto.merchandise;

import static io.irw.hawk.dto.merchandise.ProductVariantEnum.LABEDA_80_MM_WHEELS;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ProductQualifierEnum {

  NEW_LABEDA_80_MM_WHEELS(LABEDA_80_MM_WHEELS, "New"),
  USED_LABEDA_80_MM_WHEELS(LABEDA_80_MM_WHEELS, "Used");

  String name;
  ProductVariantEnum productVariant;

  private ProductQualifierEnum(ProductVariantEnum productVariant, String descriptor) {
    this.productVariant = productVariant;
    this.name = StringUtils.joinWith("/", productVariant.getName(), descriptor);
  }

}
