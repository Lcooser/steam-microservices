package com.ms.order.configs;

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

    @Value("${broker.queue.order.name}")
    private String queue;
    @Value("${broker.queue.bank.name}")
    private String bankQueue;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }
    @Bean
    public Queue bankQueue() {
        return new Queue(bankQueue, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("order.exchange");
    }

    @Bean
    public TopicExchange bankExchange() {
        return new TopicExchange("bank.exchange");
    }

    @Bean
    public Binding invoiceBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("order.invoice");
    }

    @Bean
    public Binding paymentBinding(Queue bankQueue, TopicExchange bankExchange) {
        return BindingBuilder.bind(bankQueue).to(bankExchange).with("bank.transaction");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
