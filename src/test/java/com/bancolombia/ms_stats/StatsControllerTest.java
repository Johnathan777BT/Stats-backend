package com.bancolombia.ms_stats;


import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.domain.repository.ReactiveStatsRepository;
import com.bancolombia.ms_stats.domain.service.MensajeService;
import com.bancolombia.ms_stats.domain.service.StatsService;
import com.bancolombia.ms_stats.infraestructure.rest.handler.CustomHashException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class StatsControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StatsService statsService;

    @MockBean
    private ReactiveStatsRepository statsRepository;

    @MockBean
    private MensajeService mensajeService;

    @Test
    void calcularHash_debeFuncionar() {
        Stats stats = new Stats();
        stats.setTotalContactoClientes(252);
        stats.setMotivoReclamo(25);
        stats.setMotivoGarantia(11);
        stats.setMotivoDuda(100);
        stats.setMotivoCompra(100);
        stats.setMotivoFelicitaciones(8);
        stats.setMotivoCambio(8);
        Hash hash = new Hash();
        hash.setHash("b36114a38917f5b3132afcc0c82a582d");

        when(statsService.generarHash(stats)).thenReturn(Mono.just(hash));

        webTestClient.post()
                .uri("/stats/generar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stats)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.hash").isEqualTo("b36114a38917f5b3132afcc0c82a582d");
    }

    @Test
    void guardar_debeRetornarStatus200CuandoEsExitoso() {
        Stats stats = new Stats();
        stats.setTotalContactoClientes(252);
        stats.setMotivoReclamo(25);
        stats.setMotivoGarantia(11);
        stats.setMotivoDuda(100);
        stats.setMotivoCompra(100);
        stats.setMotivoFelicitaciones(8);
        stats.setMotivoCambio(8);
        Hash hash = new Hash();
        hash.setHash("b36114a38917f5b3132afcc0c82a582d");

        when(statsService.guardar(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/stats") // Asegúrate que coincide con la ruta base del controller
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stats)
                .exchange()
                .expectStatus().isOk();

        verify(statsService).guardar(any());
    }

    @Test
    void guardar_debeRetornarErrorCuandoHashEsInvalido() {
        Stats stats = new Stats();
        stats.setHash("invalido");

        when(statsService.guardar(any())).thenReturn(
                Mono.error(new CustomHashException("El hash proporcionado no es válido."))
        );

        webTestClient.post()
                .uri("/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stats)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(response ->
                        {
                            Assertions.assertNotNull(response.getResponseBody());
                            assertThat(new String(response.getResponseBody()))
                                    .contains("El hash proporcionado no es válido.");
                        }
                );

        verify(statsService).guardar(any());
    }
}

