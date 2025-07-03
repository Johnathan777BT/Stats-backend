package com.bancolombia.ms_stats;

import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.domain.repository.ReactiveStatsRepository;
import com.bancolombia.ms_stats.domain.service.MensajeService;
import com.bancolombia.ms_stats.domain.service.StatsService;
import com.bancolombia.ms_stats.infraestructure.rest.handler.CustomHashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private ReactiveStatsRepository statsRepository;

    @Mock
    private MensajeService mensajeService;

    private StatsService statsService;

    @BeforeEach
    void setup() {
        statsService = new StatsService(statsRepository, mensajeService);
    }

    @Test
    void guardar_debeGuardarStatsSiElHashEsValido() {
        Stats stats = new Stats();
        stats.setHash("cbe265512fbfe12841355b32b725fff8");

        when(statsRepository.validarHash(stats)).thenReturn(Mono.just(true));
        when(statsRepository.guardar(any())).thenReturn(Mono.empty());

        StepVerifier.create(statsService.guardar(stats))
                .verifyComplete();

        verify(statsRepository).validarHash(stats);
        verify(mensajeService).agregarMensaje(stats);
        verify(statsRepository).guardar(stats);
    }

    @Test
    void guardar_debeLanzarErrorSiElHashEsInvalido() {
        Stats stats = new Stats();
        stats.setHash("falso");

        when(statsRepository.validarHash(stats)).thenReturn(Mono.just(false));

        StepVerifier.create(statsService.guardar(stats))
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomHashException &&
                                throwable.getMessage().equals("El hash proporcionado no es válido."))
                .verify();

        verify(statsRepository).validarHash(stats);
        verifyNoMoreInteractions(statsRepository, mensajeService);
    }

    @Test
    void generarHash_debeRetornarElHashGenerado() {
        Stats stats = new Stats();
        Hash esperado = new Hash();
        esperado.setHash("xyz789");

        when(statsRepository.generarHash(stats)).thenReturn(Mono.just(esperado));

        StepVerifier.create(statsService.generarHash(stats))
                .expectNextMatches(hash -> hash.getHash().equals("xyz789"))
                .verifyComplete();

        verify(statsRepository).generarHash(stats);
    }
}