package io.irw.hawk.entity.type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;

public class MoneySerializer
    extends JsonSerializer<BigDecimal> {

  @Override
  public void serialize(
      BigDecimal value,
      JsonGenerator jsonGenerator,
      SerializerProvider provider)
      throws IOException {
    jsonGenerator.writeString(
        value.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
    );
  }

  @Override
  public Class<BigDecimal> handledType() {
    return BigDecimal.class;
  }
}