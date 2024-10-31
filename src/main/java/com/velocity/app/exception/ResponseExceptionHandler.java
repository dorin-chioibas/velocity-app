package com.velocity.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorObject handleEntityNotFoundException(EntityNotFoundException exception) {
        logError("EntityNotFoundException message: {}", exception.getMessage());
        return ErrorObject.builder()
                .message("Entity not found in the database")
                .status(404)
                .details(exception.getMessage())
                .build();
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject handleConnectException() {
        logError("Could not connect to the database");
        return ErrorObject.builder()
                .message("Could not connect to the database")
                .status(500)
                .details("A connection could not be established")
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject handlePSQLException(SQLException sqlException, HttpServletRequest httpServletRequest) {
        logError(sqlException.getMessage(), httpServletRequest.getPathInfo());
        return ErrorObject.builder()
                .message("An error has occurred with the database")
                .status(500)
                .details(sqlException.getMessage())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException,
                                                                    HttpServletRequest httpServletRequest) {
        logError(httpRequestMethodNotSupportedException.getMessage(), httpServletRequest.getPathInfo());
        return ErrorObject.builder()
                .message("An unsupported method has been used for the request path")
                .status(500)
                .details(httpRequestMethodNotSupportedException.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logError(ex.getMessage(), request.getPathInfo());
        return ErrorObject.builder().message("An unexpected error occurred").status(500).details(ex.getMessage()).build();
    }

    public void logError(String message, String path) {
        log.error("Problem: {} at path: {}", message, path);
    }

    public void logError(String message) {
        log.error(message);
    }
}
