package com.ms.bank.configs;

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

    @Value("${broker.queue.bank.name}")
    private String bankQueue;

    @Value("${broker.exchange.name}")
    private String exchangeName;

    @Bean
    public Queue bankAccountCreationQueue() {
        return new Queue("bank.account.creation", true);
    }

    @Bean
    public TopicExchange bankExchange() {
        return new TopicExchange("bank.exchange");
    }

    @Bean
    public Queue bankTransactionQueue() {
        return new Queue("bank.transaction", true);
    }

    @Bean
    public Binding bankAccountCreationBinding(Queue bankAccountCreationQueue, TopicExchange bankExchange) {
        return BindingBuilder.bind(bankAccountCreationQueue).to(bankExchange).with("bank.create.account");
    }

    @Bean
    public Binding bankTransactionBinding(Queue bankTransactionQueue, TopicExchange bankExchange) {
        return BindingBuilder.bind(bankTransactionQueue).to(bankExchange).with("bank.transaction");
    }



    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
