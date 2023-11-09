package io.irw.hawk.scraper.service.processors.skates.parts.extractors;

import static io.irw.hawk.scraper.service.processors.skates.parts.extractors.WheelCountExtractor.extractNumberOfWheelsFromText;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class WheelCountExtractorTest {

  @Test
  public void testExtractNumberOfWheelsFromText() {
    // Test cases with known numbers of wheels
    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Gripper Ref Roller Hockey Inline Wheels 80mm X-SOFT 4 Pack NEW IN PACKAGE",
        ""
    ));

    assertEquals(Optional.of(1), extractNumberOfWheelsFromText(
        "NEW Labeda Asphalt Outdoor Gripper Roller Hockey Wheel Orange 80MM One",
        ""
    ));

    assertEquals(Optional.of(1), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Yellow Hockey Wheel 80mm X-SOFT (Single Wheel",
        ""
    ));

    assertEquals(Optional.of(1), extractNumberOfWheelsFromText(
        "One Replacement 80mm Outdoor Inline Skate rollerblade-hockey Wheel",
        ""
    ));

    // Test cases with numbers in text form
    assertEquals(Optional.of(3), extractNumberOfWheelsFromText(
        "Three Pack of 80mm Wheels for Inline Skating",
        ""
    ));

    assertEquals(Optional.of(17), extractNumberOfWheelsFromText(
        "Seventeen wheels in this set",
        ""
    ));

    assertEquals(Optional.of(20), extractNumberOfWheelsFromText(
        "Twenty pieces of wheels included",
        ""
    ));

    // Test case with a single wheel mentioned
    assertEquals(Optional.of(1), extractNumberOfWheelsFromText(
        "Labeda Gripper Roller Hockey Inline Wheel Clear 80mm X-Soft 76A",
        ""
    ));

    // Test case with singular form of "wheel"
    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda wheels",
        "Each wheel is individually packaged"
    ));

    // Test case with no mention of wheels
    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Shooter Medium 78A Roller Hockey - Green",
        ""
    ));

    assertEquals(Optional.of(8), extractNumberOfWheelsFromText(
        "Labeda Tremor 80MM Soft Roller Blade Inline Hockey Wheels asphalt 8 Wheels",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "OPEN BOX Team Labeda 80MM Clear Fear Series Set Of 4 Inline Wheels & Frames 82A",
        ""
    ));

    assertEquals(Optional.of(6), extractNumberOfWheelsFromText(
        "Labeda Millennium Roller Hockey Wheels 80MM SOFT  ( lot of 6 )",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Asphalt Inline Roller Hockey Wheels 80mm Gripper Orange/Black-4 Pack",
        ""
    ));

    assertEquals(Optional.of(8), extractNumberOfWheelsFromText(
        "Labeda Shooter Inline Roller Hockey Wheels GREEN 80mm Indoor Outdoor 8 Pack Tool",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Roller Hockey Wheels 72mm 80mm Shooters Blue White",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "LABEDA WHEELS Roller Hockey GRIPPER ASPHALT HILO 4-76mm Wheels/4-80mm Wheels",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Lazer Inline Skate Wheels",
        ""
    ));

    assertEquals(Optional.of(1), extractNumberOfWheelsFromText(
        "Labeda Shooter Outdoor Rollerblade Wheel! 78A 72mm 80mm Outdoor Indoor Marsblade",
        ""
    ));

    assertEquals(Optional.of(8), extractNumberOfWheelsFromText(
        "Labeda Shooter Inline Roller Hockey Wheels GREEN 80mm Indoor Outdoor 8 Pack",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Shooter Inline Roller Hockey Wheels HILO SET 72mm / 80mm Indoor Outdoor",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Shooter Inline Roller Hockey Wheels HILO SET 76mm / 80mm Indoor Outdoor",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Gripper Ref Roller Hockey Inline Wheels 80mm X-SOFT 4 Pack NEW IN PACKAGE",
        ""
    ));

    // Test cases with numbers in text form
    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Asphalt Inline Roller Hockey Wheels 80mm Orange 85A 4-Pack Bones Reds",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Asphalt Inline Roller Hockey Wheels 80mm White 83A 4-Pack Bones Reds",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Shooter Hockey Wheels for Roller Hockey Inline Skates",
        ""
    ));

    assertEquals(Optional.of(1), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Asphalt Outdoor Roller Hockey Inline Wheel 76 80 mm",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Roller Hockey Inline Wheels 4 Pack  - Choose Color / ",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Purple Roller Hockey Inline Wheels 80mm X-SOFT 4 Pack",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Yellow Roller Hockey Inline Wheels 80mm X-SOFT 4 Pack",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Orange Roller Hockey Inline Wheels 80mm SOFT 4 Pack",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Shooters inline skate 80mm wheels (4)",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "4 Labeda Lite Soft Gripper Inline Roller Hockey Wheels 80mm w/Bearings +1",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Set of 4 Labeda Gripper 72mm/80A wheels",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Yellow Gripper Rollerblade Wheels! 80A 72mm 80mm Outdoor Indoor Marsblade",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "LABEDA GRIPPER  Red Hot  ROLLER HOCKEY WHEEL, 80MM, ReFlex Flex  4 PACK-85A",
        ""
    ));

    assertEquals(Optional.of(4), extractNumberOfWheelsFromText(
        "Labeda inline wheels gripper 72mm medium durometer 80A (four wheels)",
        ""
    ));

    assertEquals(Optional.empty(), extractNumberOfWheelsFromText(
        "Labeda Millennium Gripper Roller Hockey Inline Wheels - Choose Color / Size",
        ""
    ));
  }
}