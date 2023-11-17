package io.irw.hawk.repository.converters;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class ProductVariantEnumConverter implements AttributeConverter<ProductVariantEnum, String> {

  private static final Map<String, ProductVariantEnum> TO_STRING_MAP = new HashMap<>();

  static {
    for (ProductVariantEnum enumValue : ProductVariantEnum.values()) {
      String key = enumValue.toString();
      if (TO_STRING_MAP.containsKey(key)) {
        throw new IllegalStateException("Duplicate toString representation: " + key);
      }
      TO_STRING_MAP.put(key, enumValue);
    }
  }

  @Override
  public String convertToDatabaseColumn(ProductVariantEnum attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.toString();
  }

  @Override
  public ProductVariantEnum convertToEntityAttribute(String dbData) {
    ProductVariantEnum enumValue = TO_STRING_MAP.get(dbData);
    if (enumValue != null) {
      return enumValue;
    }
    throw new IllegalArgumentException("Unknown enum value: " + dbData);
  }
}
