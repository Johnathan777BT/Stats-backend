package com.bancolombia.ms_stats.infraestructure.bus;

import com.bancolombia.ms_stats.infraestructure.bus.repository.StatsData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(StatsData statsData) {

        System.out.println("Total contactos: " + statsData.getStats().getTotalContactoClientes());
    }
}