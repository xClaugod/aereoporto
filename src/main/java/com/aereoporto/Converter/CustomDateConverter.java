package com.aereoporto.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class CustomDateConverter implements AttributeConverter<Date, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Convert a Date to its database representation as a String.
     * Null Dates are converted to null Strings.
     * 
     * @param attribute the Date to convert
     * @return the String representation of the Date
     */
    @Override
    public String convertToDatabaseColumn(Date attribute) {
        if (attribute == null)
            return null;
        return formatter.format(attribute.toLocalDate());
    }

    /**
     * Convert a String in the database representation to its Date object.
     * Null Strings are converted to null Dates.
     * 
     * @param dbData the String to convert
     * @return the Date object
     */
    @Override
    public Date convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        LocalDate ld = LocalDate.parse(dbData, formatter);
        return Date.valueOf(ld);
    }
}
