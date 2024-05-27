package com.sa.habitTrackerBackend.commons.exception;

import lombok.Getter;

@Getter
public abstract class AbstractCheckedException extends Exception {

    protected final transient Object details;
    protected final String code;

    protected AbstractCheckedException(String message, Object details, String code) {
        super(message);
        this.details = details;
        this.code = code;
    }

    protected AbstractCheckedException(Throwable cause, String message, Object details, String code) {
        super(message, cause);
        this.code = code;
        this.details = details;
    }
}