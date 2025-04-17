package com.aereoporto.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class CustomTimestampConverter implements AttributeConverter<Timestamp, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss,nnnnnnnnn");

    @Override
    public String convertToDatabaseColumn(Timestamp attribute) {
        if (attribute == null)
            return null;
        return formatter.format(attribute.toLocalDateTime());
    }

    @Override
    public Timestamp convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        LocalDateTime ldt = LocalDateTime.parse(dbData, formatter);
        return Timestamp.valueOf(ldt);
    }
}
