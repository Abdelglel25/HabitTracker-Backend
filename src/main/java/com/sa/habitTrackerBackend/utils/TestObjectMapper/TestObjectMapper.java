package com.sa.habitTrackerBackend.utils.TestObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class TestObjectMapper {
    private final ObjectMapper objectMapper;

    public TestObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> String toJson(T dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}
