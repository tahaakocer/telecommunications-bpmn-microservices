package com.tahaakocer.externalapiservice.exception;


import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse<?>> handleGeneralException(Exception ex) {
        GeneralResponse<?> errorResponse = GeneralResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .data(null)
                .build();
        log.error("{}:{}", errorResponse.getMessage(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<GeneralResponse<?>> handleGeneralException(GeneralException ex) {
        GeneralResponse<?> errorResponse = GeneralResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .data(null)
                .build();
        log.error("{}:{}", errorResponse.getMessage(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
