package com.bancolombia.ms_stats.infraestructure.rest.handler;

import com.bancolombia.ms_stats.infraestructure.rest.handler.error.ErrorRespuesta;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class ManejoErroresGlobal {

    @ExceptionHandler(CustomHashException.class)
    public ResponseEntity<ErrorRespuesta> manejarHashInvalido(CustomHashException ex) {
        ErrorRespuesta error = new ErrorRespuesta(400, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
