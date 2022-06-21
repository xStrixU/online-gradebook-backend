package me.xstrixu.onlinegradebook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    ResponseEntity<Object> resourceNotFound(ResourceNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<Object> badCredentials(AuthenticationException ex) {
        return handleException(ex, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Object> handleException(Exception exception, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>(){{
            put("timestamp", System.currentTimeMillis());
            put("status", status.value());
            put("message", exception.getMessage());
        }};

        return new ResponseEntity<>(body, status);
    }
}
