package com.bancolombia.ms_stats.infraestructure.rest.handler.error;

import lombok.Data;

@Data
public class ErrorRespuesta {
    private int codigo;
    private String mensaje;

    public ErrorRespuesta(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }
}
