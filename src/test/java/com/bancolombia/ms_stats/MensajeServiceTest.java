package com.bancolombia.ms_stats;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.domain.service.MensajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class MensajeServiceTest {

    private MensajeService mensajeService;

    @BeforeEach
    void setUp() {
        mensajeService = new MensajeService();
    }

    @Test
    void agregarMensaje_deberiaAgregarStatsALaLista() {
        Stats stats = new Stats();
        stats.setTotalContactoClientes(100);

        mensajeService.agregarMensaje(stats);

        List<Stats> mensajes = mensajeService.obtenerMensajes();

        assertEquals(1, mensajes.size());
        assertEquals(100, mensajes.get(0).getTotalContactoClientes());
    }

    @Test
    void obtenerMensajes_deberiaRetornarListaVaciaInicialmente() {
        List<Stats> mensajes = mensajeService.obtenerMensajes();

        assertTrue(mensajes.isEmpty());
    }
}
