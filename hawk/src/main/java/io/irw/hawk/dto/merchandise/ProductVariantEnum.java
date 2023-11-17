package io.irw.hawk.dto.merchandise;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ProductVariantEnum {

  LABEDA_80_MM_WHEELS(GroupEnum.WHEELS, "Labeda 80mm wheels");

  String name;
  GroupEnum group;

  private ProductVariantEnum(GroupEnum group, String descriptor) {
    this.group = group;
    this.name = StringUtils.joinWith("/", group.getName(), descriptor);
  }

  @Override
  public String toString() {
    return name;
  }

}
