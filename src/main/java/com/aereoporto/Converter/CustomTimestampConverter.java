package com.aereoporto.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class CustomTimestampConverter implements AttributeConverter<Timestamp, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss,nnnnnnnnn");

    /**
     * Convert a Timestamp to its database representation as a String.
     * Null Timestamps are converted to null Strings.
     * 
     * @param attribute the Timestamp to convert
     * @return the String representation of the Timestamp
     */
    @Override
    public String convertToDatabaseColumn(Timestamp attribute) {
        if (attribute == null)
            return null;
        return formatter.format(attribute.toLocalDateTime());
    }

    /**
     * Convert a String in the database representation to its Timestamp object.
     * Null Strings are converted to null Timestamps.
     * 
     * @param dbData the String to convert
     * @return the Timestamp object
     */
    @Override
    public Timestamp convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        LocalDateTime ldt = LocalDateTime.parse(dbData, formatter);
        return Timestamp.valueOf(ldt);
    }
}
