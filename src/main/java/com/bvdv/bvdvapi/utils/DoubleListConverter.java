package com.bvdv.bvdvapi.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Converter
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<Double> stringList) {
        return stringList != null ? stringList.stream().map(Object::toString).collect(Collectors.joining(SPLIT_CHAR)) : "";
    }

    @Override
    public List<Double> convertToEntityAttribute(String string) {
        return string != null ? Arrays.stream(string.split(SPLIT_CHAR)).map(Double::parseDouble).collect(Collectors.toList()) : emptyList();
    }
}