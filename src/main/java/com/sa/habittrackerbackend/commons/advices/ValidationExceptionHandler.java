package com.sa.habittrackerbackend.commons.advices;

import com.sa.habittrackerbackend.commons.exception.BadRequestException;
import com.sa.habittrackerbackend.dto.auth.ApiErrorDto;
import io.micrometer.common.lang.NonNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.*;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MESSAGE_STRING = "message";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, Object> body = buildErrorBody((ServletWebRequest) request, (HttpStatus) status);
        body.put(MESSAGE_STRING, "There are invalid or missing fields");

        // Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        Map<String, Object> body = buildErrorBody((ServletWebRequest) request, status);
        body.put(MESSAGE_STRING,ex.getMessage());
        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, @NonNull WebRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        Map<String, Object> body = buildErrorBody((ServletWebRequest) request, status);
        body.put(MESSAGE_STRING,ex.getMessage());
        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorDto> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(
                new ApiErrorDto(exception.getMessage(), exception.getDetails(), exception.getCode()),
                HttpStatus.BAD_REQUEST
        );
    }

    private Map<String, Object> buildErrorBody(ServletWebRequest request, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getRequest().getRequestURI());
        return body;
    }
}
