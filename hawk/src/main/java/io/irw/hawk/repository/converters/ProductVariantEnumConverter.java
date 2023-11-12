package io.irw.hawk.repository.converters;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductVariantEnumConverter implements AttributeConverter<ProductVariantEnum, String> {

  @Override
  public String convertToDatabaseColumn(ProductVariantEnum attribute) {
    return attribute.toString();
  }

  @Override
  public ProductVariantEnum convertToEntityAttribute(String dbData) {
    // Iterate over enum values to find the matching one based on toString()
    for (ProductVariantEnum enumValue : ProductVariantEnum.values()) {
      if (enumValue.toString().equals(dbData)) {
        return enumValue;
      }
    }
    throw new IllegalArgumentException("Unknown enum value: " + dbData);
  }
}
