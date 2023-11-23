package com.okey.drone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.EnumSet;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private static final EnumSet<HttpStatus> EXCEPTION_STATUSES = EnumSet.of(
            HttpStatus.INTERNAL_SERVER_ERROR
    );


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return this.buildCustomExceptionResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        return this.buildCustomExceptionResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildCustomExceptionResponse(Exception e, WebRequest request, HttpStatus httpStatus) {
        if (EXCEPTION_STATUSES.contains(httpStatus)) {
            log.error("Exception occurred...", e);
        }

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, httpStatus);
    }
}
