package com.sa.habittrackerbackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorDto {

    private ZonedDateTime timestamp;
    private String message;
    private Object details;
    private String code;
    private Integer status;

    public ApiErrorDto(String message, Object details, String code) {
        this(ZonedDateTime.now(), message, details, code, null);
    }

    public ApiErrorDto(String message, Object details, String code, Integer status) {
        this(ZonedDateTime.now(), message, details, code, status);
    }

}