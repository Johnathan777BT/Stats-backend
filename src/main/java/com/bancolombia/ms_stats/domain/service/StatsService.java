package com.bancolombia.ms_stats.domain.service;

import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.domain.repository.ReactiveStatsRepository;
import com.bancolombia.ms_stats.infraestructure.rest.handler.CustomHashException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class StatsService {

    private final ReactiveStatsRepository statsRepository;
    private final MensajeService mensajeService;

    public StatsService(ReactiveStatsRepository statsRepository, MensajeService mensajeService) {
        this.statsRepository = statsRepository;
        this.mensajeService = mensajeService;
    }

    public Mono<Void> guardar(Stats stats) {
        return statsRepository.validarHash(stats)
                .flatMap(hashValido -> {
                    if (!hashValido) {
                        return Mono.error(new CustomHashException("El hash proporcionado no es válido."));
                    }
                    stats.setTimestamp(Instant.now());
                    mensajeService.agregarMensaje(stats);
                    return statsRepository.guardar(stats);
                });
    }

    public Mono<Hash> generarHash(Stats stats) {
        return statsRepository.generarHash(stats);
    }
}