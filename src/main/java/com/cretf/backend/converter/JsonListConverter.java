package com.cretf.backend.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class JsonListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            if (dbData.startsWith("[[")) {
                // Trường hợp double-nested JSON array
                List<List<String>> nestedList = objectMapper.readValue(dbData, new TypeReference<List<List<String>>>() {});
                if (!nestedList.isEmpty()) {
                    return nestedList.get(0);
                }
                return new ArrayList<>();
            } else {
                // Trường hợp JSON array thông thường
                return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
            }
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}