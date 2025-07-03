package com.bancolombia.ms_stats.domain.repository;

import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import reactor.core.publisher.Mono;

import java.time.Instant;

public interface ReactiveStatsRepository {
    Mono<Void> guardar(Stats stats);
    Mono<Stats> obtenerPorTimestamp(Instant timestamp);
    Mono<Boolean> validarHash(Stats stats);
    Mono<Hash> generarHash(Stats stats);
}
