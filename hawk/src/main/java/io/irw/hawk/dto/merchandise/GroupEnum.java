package io.irw.hawk.dto.merchandise;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum GroupEnum {

  WHEELS( CategoryEnum.SKATE_PARTS, "Wheels");

  String name;
  CategoryEnum category;

  private GroupEnum(CategoryEnum category, String descriptor) {
    this.category = category;
    this.name = StringUtils.joinWith("/", category.getName(), descriptor);
  }

}
