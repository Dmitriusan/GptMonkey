package io.irw.hawk.scraper.service.processors.skates.parts.extractors;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.dto.merchandise.GroupEnum;
import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import io.irw.hawk.scraper.ai.AIEnhanced;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import io.irw.hawk.scraper.model.ProcessingPipelineStep;
import io.irw.hawk.scraper.service.extractors.ItemSummaryDataExtractor;
import io.irw.hawk.scraper.service.extractors.PriceExtractor;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WheelCountExtractor implements ItemSummaryDataExtractor {

  @Override
  public List<Class<? extends ProcessingPipelineStep>> dependencyFor() {
    return List.of(PriceExtractor.class);
  }

  @Override
  public boolean isApplicableTo(ProductVariantEnum productVariant) {
    return productVariant.getGroup().equals(GroupEnum.WHEELS);
  }

  @Override
  public void extractDataFromItemSummary(ItemSummary itemSummary, MerchandiseMetadataDto metadata) {
    String title = itemSummary.getTitle();
    String shortDescription = itemSummary.getShortDescription();

    var numberOfWheels = extractNumberOfWheelsFromText(title, shortDescription);
    //.or();

    // TODO: rewrite text to json
    // TODO: maybe rewrite to iterate from 1 to 40 and try to match each number
    // TODO: match also like "Labeda Shooters inline skate 80mm wheels (4)"
    // TODO: match also like "4 Labeda Lite Soft Gripper Inline Roller Hockey Wheels 80mm w/Bearings +1"

    // TODO: default pack by wheel size (4/8 for 80mm)
    // TODO: match by price
    metadata.getEbayFindingDto().setNumberOfPieces(numberOfWheels);
  }

  protected static Optional<Integer> extractNumberOfWheelsFromText(String titleWithMixedCase, String shortDescriptionWithMixedCase) {
    String title = titleWithMixedCase.toLowerCase();
    String shortDescription = lowerCase(shortDescriptionWithMixedCase);
    // Try to match an exact number
    String[] multipleWheelPatterns = {
        "\\b(\\d+)\\s*\\-?\\s*(?:pieces|pcs|pack|set|wheels)\\b",
        "\\b(?:lot|set|pack)\\s+of\\s+(\\d+)",
    };
    String mergedTitleAndDescription = mergedTexts(title, shortDescription);
    for (String patternStr : multipleWheelPatterns) {
      Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(mergedTitleAndDescription);

      if (matcher.find()) {
        String match = matcher.group(1); // Extract the matched number
        int value = Integer.parseInt(match);
        if (value <= 40) { // To avoid matching like 80mm
          return Optional.of(value);
        }
      }
    }

    // Try to match a number text from "two" to "twenty"
    String textNumbers = "two|three|four|five|six|seven|eight|nine|ten|eleven|twelve|thirteen|fourteen|fifteen|"
        + "sixteen|seventeen|eighteen|nineteen|twenty";
    String[] numberInTextFormPatterns = {
        "\\b(" + textNumbers + ")\\s*\\-?\\s*(?:pieces|pcs|pack|set|wheels)\\b",
        "\\b(?:lot|set|pack)\\s+of\\s+(" + textNumbers + ")",
    };
    for (String patternStr : numberInTextFormPatterns) {
      Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(mergedTitleAndDescription);

      if (matcher.find()) {
        String match = matcher.group(1);
        return convertTextNumberToNumericValue(match);
      }
    }

    // Try to match a single wheel
    String[] singleWheelPatterns = {"\\b(one|1)\\s*(?:wheel)\\b",};
    for (String patternStr : singleWheelPatterns) {
      Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(mergedTitleAndDescription);

      if (matcher.find()) {
        return Optional.of(1);
      }
    }

    // TODO: work around 1-day shipment, 2/3/two day shipping and so on
    // TODO: match also like "HILO 4-76mm Wheels/4-80mm Wheels"

    // Try to match a single wheel by singular form
    if (containsIgnoreCase(mergedTitleAndDescription, "wheel")
        || containsIgnoreCase(mergedTitleAndDescription, "one")
        || containsIgnoreCase(mergedTitleAndDescription, "single")) {
      String[] negativeSingularFormPatterns = {
          "\\b(each|every|any)\\s*(?:wheel)\\b",
          "wheels"
      };
      boolean singularWheelWordFound = true;
      for (String patternStr : negativeSingularFormPatterns) {
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mergedTitleAndDescription);

        if (matcher.find()) {
          singularWheelWordFound = false;
        }
      }
      if (singularWheelWordFound) {
        return Optional.of(1);
      }
    }

    // Try to match just text numbers
    String[] justNumberInTextFormPatterns = {
        "(two|three|four|five|six|seven|eight|nine|ten|eleven|twelve|thirteen|fourteen|fifteen|"
            + "sixteen|seventeen|eighteen|nineteen|twenty)",};
    for (String patternStr : justNumberInTextFormPatterns) {
      Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(mergedTitleAndDescription);

      if (matcher.find()) {
        String match = matcher.group(1);
        return convertTextNumberToNumericValue(match);
      }
    }

    return Optional.empty();
  }

  @NotNull
  private static String mergedTexts(String title, String shortDescription) {
    return title + " " + StringUtils.defaultString(shortDescription, "");
  }

  private static Optional<Integer> convertTextNumberToNumericValue(String textNumber) {
    switch (textNumber) {
      case "one":
        return Optional.of(1);
      case "two":
        return Optional.of(2);
      case "three":
        return Optional.of(3);
      case "four":
        return Optional.of(4);
      case "five":
        return Optional.of(5);
      case "six":
        return Optional.of(6);
      case "seven":
        return Optional.of(7);
      case "eight":
        return Optional.of(8);
      case "nine":
        return Optional.of(9);
      case "ten":
        return Optional.of(10);
      case "eleven":
        return Optional.of(11);
      case "twelve":
        return Optional.of(12);
      case "thirteen":
        return Optional.of(13);
      case "fourteen":
        return Optional.of(14);
      case "fifteen":
        return Optional.of(15);
      case "sixteen":
        return Optional.of(16);
      case "seventeen":
        return Optional.of(17);
      case "eighteen":
        return Optional.of(18);
      case "nineteen":
        return Optional.of(19);
      case "twenty":
        return Optional.of(20);
      default:
        return Optional.empty(); // Invalid text number
    }
  }

}
