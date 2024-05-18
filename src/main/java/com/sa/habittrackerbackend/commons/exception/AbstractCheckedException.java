package com.sa.habittrackerbackend.commons.exception;

import lombok.Getter;

@Getter
public abstract class AbstractCheckedException extends Exception {

    protected final Object details;
    protected final String code;

    public AbstractCheckedException(String message, Object details, String code) {
        super(message);
        this.details = details;
        this.code = code;
    }

    public AbstractCheckedException(Throwable cause, String message, Object details, String code) {
        super(message, cause);
        this.code = code;
        this.details = details;
    }
}