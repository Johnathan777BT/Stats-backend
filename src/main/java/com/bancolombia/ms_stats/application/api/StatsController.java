package com.bancolombia.ms_stats.application.api;

import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.domain.repository.ReactiveStatsRepository;
import com.bancolombia.ms_stats.domain.service.MensajeService;
import com.bancolombia.ms_stats.domain.service.StatsService;
import com.bancolombia.ms_stats.infraestructure.rest.handler.CustomHashException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final ReactiveStatsRepository statsRepository;
    private final StatsService statsService;
    @Autowired
    private MensajeService mensajeService;

    public StatsController(ReactiveStatsRepository statsRepository, StatsService statsService) {
        this.statsRepository = statsRepository;
        this.statsService = statsService;
    }

    @PostMapping
    public Mono<Void> guardar(@RequestBody Mono<Stats> requestMono) {
        return requestMono.flatMap(statsService::guardar);
    }

    @GetMapping("/{timestamp}")
    public Mono<Stats> obtenerPorTimestamp(@PathVariable String timestamp) {
        Instant clave = Instant.parse(timestamp);
        return statsRepository.obtenerPorTimestamp(clave);
    }

    @GetMapping("/mensajes")
    public List<Stats> obtenerMensajes() {
        return mensajeService.obtenerMensajes();
    }


    @PostMapping("/generar")
    public Mono<Hash> calcularHash(@RequestBody Mono<Stats> requestMono){
        return requestMono.flatMap(statsService::generarHash);
    }
}
