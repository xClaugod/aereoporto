package com.aereoporto.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class CustomDateConverter implements AttributeConverter<Date, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public String convertToDatabaseColumn(Date attribute) {
        if (attribute == null)
            return null;
        return formatter.format(attribute.toLocalDate());
    }

    @Override
    public Date convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        LocalDate ld = LocalDate.parse(dbData, formatter);
        return Date.valueOf(ld);
    }
}
