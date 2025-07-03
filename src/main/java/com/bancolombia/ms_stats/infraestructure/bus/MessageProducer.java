package com.bancolombia.ms_stats.infraestructure.bus;

import com.bancolombia.ms_stats.infraestructure.bus.repository.StatsData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(StatsData statsData) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, statsData);
        System.out.println("Mensaje enviado: " + statsData);
    }
}