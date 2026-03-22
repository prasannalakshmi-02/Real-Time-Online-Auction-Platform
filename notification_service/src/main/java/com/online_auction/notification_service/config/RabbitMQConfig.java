package com.online_auction.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;



@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_QUEUE = "email_queue";

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public Queue auctionResultQueue() {
        return new Queue("auction.result.queue", true);
    }

    // 2. Define the Exchange
    @Bean
    public TopicExchange auctionExchange() {
        return new TopicExchange("auction.exchange"); }

    @Bean
    public Binding binding(Queue auctionResultQueue, TopicExchange auctionExchange) {
        return BindingBuilder.bind(auctionResultQueue)
                .to(auctionExchange)
                .with("auction.result");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
