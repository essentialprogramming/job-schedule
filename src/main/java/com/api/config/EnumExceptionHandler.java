package com.api.config;

import com.api.output.ExceptionJSON;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class EnumExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ExceptionJSON> handleException() {

        ExceptionJSON outputException = ExceptionJSON.builder()
                .status("400")
                .error("Bad Request")
                .message("Invalid review status! Must be of value: ACCEPTED, REJECTED, CHANGES_REQUIRED")
                .timestamp(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(outputException, HttpStatus.BAD_REQUEST);
    }
}
