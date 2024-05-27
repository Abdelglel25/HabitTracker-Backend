package com.sa.habitTrackerBackend.commons.exception;

public class BadRequestException extends AbstractCheckedException {
    public BadRequestException(String message, Object details, String code) {
        super(message, details, code);
    }

    public BadRequestException(Throwable cause, String message, Object details, String code) {
        super(cause, message, details, code);
    }
}