package io.irw.hawk.entity.type;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier;

public class CustomObjectMapperSupplier
    implements ObjectMapperSupplier {

  @Override
  public ObjectMapper get() {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    // Configure it to use ISO 8601 date format for serialization
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDateFormat(new StdDateFormat());

    SimpleModule customBigDecimalMoneyModule = new SimpleModule(
        "CustomBigDecimalMoneyModule",
        new Version(1, 0, 0, null, null, null)
    );
    customBigDecimalMoneyModule.addSerializer(new MoneySerializer());

    objectMapper.registerModule(customBigDecimalMoneyModule);

    return objectMapper;
  }
}