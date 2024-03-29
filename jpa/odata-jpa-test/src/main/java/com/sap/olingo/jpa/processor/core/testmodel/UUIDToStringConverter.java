package com.sap.olingo.jpa.processor.core.testmodel;

import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Default converter to convert from {@link java.util.UUID} to a byte array.
 *
 * @author Oliver Grande
 */
@Converter(autoApply = false)
public class UUIDToStringConverter implements AttributeConverter<UUID, String> {

  @Override
  public String convertToDatabaseColumn(final UUID uuid) {
    return uuid == null ? null : uuid.toString();
  }

  @Override
  public UUID convertToEntityAttribute(final String dbData) {
    return dbData == null ? null : UUID.fromString(dbData);
  }
}
