package com.bancolombia.ms_stats.domain.service;

import com.bancolombia.ms_stats.domain.model.Stats;
import org.springframework.stereotype.Service;
import com.bancolombia.ms_stats.infraestructure.bus.RabbitMQConfig;
import com.bancolombia.ms_stats.infraestructure.bus.repository.StatsData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MensajeService {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MensajeService.class);
    private final List<Stats> mensajes = new CopyOnWriteArrayList<>();

    public MensajeService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void agregarMensaje(Stats stats) {
        logger.info("Iniciando envío de mensaje a RabbitMQ para stats: {}", stats.getTimestamp());
        mensajes.add(stats);
        // Enviar a RabbitMQ
        StatsData statsData = new StatsData();
        statsData.setStats(stats);

        try {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            statsData );
            logger.info("Mensaje enviado exitosamente a exchange: {}, routing key: {}",  RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY);
        } catch (Exception e) {
            logger.error("Error al enviar mensaje a RabbitMQ", e);
        }
    }

    public List<Stats> obtenerMensajes() {
        return mensajes;
    }
}