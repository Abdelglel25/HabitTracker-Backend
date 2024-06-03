package com.sa.habitTrackerBackend.utils.TestObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestObjectMapper {
    private final ObjectMapper objectMapper;

    public TestObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> String toJson(T dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public <T> T toObject(String contentAsString, Class<T> tClass) throws IOException {
        return objectMapper.readValue(contentAsString, tClass);
    }
}
