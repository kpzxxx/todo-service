package com.simplesystem.todo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {

    log.warn("Business exception: status={}, code={}, message={}",
        ex.getStatus(), ex.getCode(), ex.getMessage());

    ErrorResponse error = new ErrorResponse(
        ex.getStatus().value(),
        ex.getCode(),
        ex.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(ex.getStatus()).body(error);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException ex) {

    log.warn("Resource not found: {}", ex.getMessage());

    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "RESOURCE_NOT_FOUND",
        ex.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

    log.error("Unexpected error", ex);

    ErrorResponse error = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "INTERNAL_SERVER_ERROR",
        "Internal server error",
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}

