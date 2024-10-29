package com.yagodaoud.comandae.controller;

import com.yagodaoud.comandae.exception.ErrorResponse;
import com.yagodaoud.comandae.exception.OrderSlipInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderSlipInUseException.class)
    public ResponseEntity<ErrorResponse> handleOrderSlipInUseException(OrderSlipInUseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
