package com.bancolombia.ms_stats.infraestructure.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);
    public static final String QUEUE = "event.stats.validated";
    public static final String EXCHANGE = "event.stats.exchange";
    public static final String ROUTING_KEY = "event.stats.routing-key";

    @Bean
    public Queue queue() {
        logger.info("creando cola: {}", QUEUE);
        return new Queue(QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
        logger.info("creando exchange: {}", EXCHANGE);
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        logger.info("creando binding entre: {} y {} con routing key: {}", QUEUE, EXCHANGE, ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
