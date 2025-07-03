package com.bancolombia.ms_stats.infraestructure.rest.handler;

public class CustomHashException  extends RuntimeException {
    public CustomHashException(String mensaje) {
        super(mensaje);
    }
}
