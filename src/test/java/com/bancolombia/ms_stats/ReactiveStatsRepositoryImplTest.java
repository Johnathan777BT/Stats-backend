package com.bancolombia.ms_stats;

import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.infraestructure.dynamodb.ReactiveStatsRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactiveStatsRepositoryImplTest {

    @Mock
    private DynamoDbAsyncTable<Stats> tablaMock;

    private ReactiveStatsRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ReactiveStatsRepositoryImpl(tablaMock);
    }

    @Test
    void guardar_deberiaGuardarCorrectamente() {
        Stats stats = new Stats();
        when(tablaMock.putItem(stats)).thenReturn(CompletableFuture.completedFuture(null));

        Mono<Void> result = repository.guardar(stats);

        StepVerifier.create(result)
                .verifyComplete();

        verify(tablaMock).putItem(stats);
    }

    @Test
    void generarHash_deberiaRetornarHashCalculado() {
        Stats stats = new Stats();
        stats.setTotalContactoClientes(3);
        stats.setMotivoReclamo(25);
        stats.setMotivoGarantia(11);
        stats.setMotivoDuda(100);
        stats.setMotivoCompra(100);
        stats.setMotivoFelicitaciones(7);
        stats.setMotivoCambio(8);

        Mono<Hash> result = repository.generarHash(stats);

        StepVerifier.create(result)
                .assertNext(hash -> assertNotNull(hash.getHash()))
                .verifyComplete();
    }

    @Test
    void validarHash_deberiaRetornarTrueParaHashCorrecto() {
        Stats stats = new Stats();
        stats.setTotalContactoClientes(251);
        stats.setMotivoReclamo(25);
        stats.setMotivoGarantia(11);
        stats.setMotivoDuda(100);
        stats.setMotivoCompra(100);
        stats.setMotivoFelicitaciones(7);
        stats.setMotivoCambio(8);
        stats.setHash("d9a2999d1eeff8b97a4e9b572ad35489");

        Mono<Boolean> resultado = repository.validarHash(stats);

        StepVerifier.create(resultado)
                .expectNext(true)
                .verifyComplete();
    }
}
