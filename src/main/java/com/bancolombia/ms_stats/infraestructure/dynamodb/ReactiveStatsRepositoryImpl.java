package com.bancolombia.ms_stats.infraestructure.dynamodb;

import com.bancolombia.ms_stats.domain.dto.Hash;
import com.bancolombia.ms_stats.domain.model.Stats;
import com.bancolombia.ms_stats.domain.repository.ReactiveStatsRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.security.MessageDigest;
import java.time.Instant;

@Component
public class ReactiveStatsRepositoryImpl implements ReactiveStatsRepository {

    private final DynamoDbAsyncTable<Stats> tabla;

    public ReactiveStatsRepositoryImpl(DynamoDbAsyncTable<Stats> tabla) {
        this.tabla = tabla;
    }

    @Override
    public Mono<Void> guardar(Stats stats) {
        return Mono.fromFuture(tabla.putItem(stats));
    }

    @Override
    public Mono<Stats> obtenerPorTimestamp(Instant timestamp) {
        return Mono.fromFuture(
                tabla.getItem(Key.builder().partitionValue(String.valueOf(timestamp)).build())
        );
    }

    @Override
    public Mono<Boolean> validarHash(Stats json) {
        return Mono.fromSupplier(() -> {
            String dataConcatenada =  String.valueOf(json.getTotalContactoClientes()) +
                    json.getMotivoReclamo() +
                    json.getMotivoGarantia() +
                    json.getMotivoDuda() +
                    json.getMotivoCompra() +
                    json.getMotivoFelicitaciones() +
                    json.getMotivoCambio();

            String hashCalculado = generateMD5(dataConcatenada);
            String hashOriginal = json.getHash();

            return hashCalculado.equalsIgnoreCase(hashOriginal);
        });
    }

    @Override
    public Mono<Hash> generarHash(Stats json) {
        return Mono.fromSupplier(() -> {
            String dataConcatenada =  String.valueOf(json.getTotalContactoClientes()) +
                    json.getMotivoReclamo() +
                    json.getMotivoGarantia() +
                    json.getMotivoDuda() +
                    json.getMotivoCompra() +
                    json.getMotivoFelicitaciones() +
                    json.getMotivoCambio();

            String hashCalculated = generateMD5(dataConcatenada);
            Hash hash = new Hash();
            hash.setHash(hashCalculated);
            return hash;
        });
    }

    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar MD5", e);
        }
    }
}
