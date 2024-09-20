package com.ms.user.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.company.name}")
    private String companyQueueName;

    @Value("${broker.queue.player.name}")
    private String playerQueueName;

    @Value("${broker.exchange.name}")
    private String exchangeName;


    @Bean
    public Queue playerQueue() {
        return new Queue(playerQueueName, true);
    }

    @Bean
    public Queue companyQueue() {
        return new Queue(companyQueueName, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding playerBinding(Queue playerQueue, TopicExchange exchange) {
        return BindingBuilder.bind(playerQueue).to(exchange).with("user.player");
    }

    @Bean
    public Binding companyBinding(Queue companyQueue, TopicExchange exchange) {
        return BindingBuilder.bind(companyQueue).to(exchange).with("user.company");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
