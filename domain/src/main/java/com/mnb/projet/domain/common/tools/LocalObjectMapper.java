package com.mnb.projet.domain.common.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnb.projet.domain.common.exceptions.DomainInternalException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalObjectMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T convertObject(String jsonString, Class<T> objectClass) {

        try {
            JsonNode jsonObj = mapper.readTree(jsonString);
            return mapper.convertValue(jsonObj, objectClass);
        } catch (JsonProcessingException e) {
            throw new DomainInternalException(DomainInternalException.DEFAULT_ERROR);
        }
    }

    public static <T> String convertToString(T obj) {

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new DomainInternalException(DomainInternalException.DEFAULT_ERROR);
        }
    }

    public static <T, R> R convertObjectToObject(T input, Class<R> outPut) {

        final String inputToString = convertToString(input);
        return convertObject(inputToString, outPut);
    }
}
