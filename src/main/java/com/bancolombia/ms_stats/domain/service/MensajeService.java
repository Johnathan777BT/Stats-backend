package com.bancolombia.ms_stats.domain.service;

import com.bancolombia.ms_stats.domain.model.Stats;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MensajeService {

    private final List<Stats> mensajes = new CopyOnWriteArrayList<>();

    public void agregarMensaje(Stats stats) {
        mensajes.add(stats);
    }

    public List<Stats> obtenerMensajes() {
        return mensajes;
    }
}