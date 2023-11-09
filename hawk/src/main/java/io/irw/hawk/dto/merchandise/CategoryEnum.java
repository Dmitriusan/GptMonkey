package io.irw.hawk.dto.merchandise;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CategoryEnum {

  SKATE_PARTS(ChapterEnum.SKATES, "Parts");

  private final String name;
  private final ChapterEnum chapter;

  private CategoryEnum(ChapterEnum chapter, String descriptor) {
    this.chapter = chapter;
    this.name = StringUtils.joinWith("/", chapter.getName(), descriptor);
  }
}
