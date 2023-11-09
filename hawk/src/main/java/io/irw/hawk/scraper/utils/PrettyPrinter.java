package io.irw.hawk.scraper.utils;

import com.ebay.buy.browse.model.ItemSummary;
import io.irw.hawk.scraper.model.MerchandiseMetadataDto;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
@Slf4j
public class PrettyPrinter {

  public void printSplitter() {
    log.info(StringUtils.repeat("-", 100));
  }

  public void prettyPrint(ItemSummary itemSummary, MerchandiseMetadataDto metadataDto) {
    String priceWithShippingCost = metadataDto.getMinShippingCostUsd()
        .map(amount -> amount + metadataDto.getTotalPriceUsd())
        .map(amount -> String.format("%.2f", amount))
        .orElse("NO SHIPPING COST");
    String numberOfPieces = metadataDto.getNumberOfPieces()
        .map(n -> Integer.toString(n))
        .orElse("_");
    String itemInfoLine = String.format("%s  |  %s  |  %s pcs  |  %s USD  |  %s  |  %s %n",
        metadataDto.getFinalVerdict(), itemSummary.getTitle(),
        numberOfPieces,
        priceWithShippingCost,
        itemSummary.getItemWebUrl(), metadataDto.getReasoning().toString());
    log.info(itemInfoLine);
    String itemSummaryWithFolding = Arrays.stream(itemSummary.toString().split("\n"))
        .map(line -> "# " + line)
        .reduce((accumulator, nextLine) -> accumulator + "\n" + nextLine)
        .orElse("");

    log.debug("# {}", itemSummaryWithFolding);
  }

}
