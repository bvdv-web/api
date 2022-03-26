package com.bvdv.bvdvapi.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    //500
    //ex
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class, Exception.class})
    public ResponseEntity<?> handleInternal(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body("Internal server error");

    }

    //404
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleNoHandlerFound(HttpClientErrorException ex) {
        return ResponseEntity.badRequest().body(ex.getStatusText());
    }

}