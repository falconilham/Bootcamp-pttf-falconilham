package com.ptff.qsystem.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// Helper to handle converting to/from LocalDateTime. Hibernate doesn't handle it well by default.
@Converter(autoApply = true)
public class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
        return null == entityValue ? null : Timestamp.valueOf(entityValue);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        return null == databaseValue ? null : databaseValue.toLocalDateTime();
    }
}
