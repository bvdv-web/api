package com.bvdv.bvdvapi.service;

import com.bvdv.bvdvapi.config.RabbitMqConfig;
import com.bvdv.bvdvapi.models.BvdvRequest;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BvdvRabbitmqProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    TopicExchange topicExchange;

    public void produce(BvdvRequest request) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.BVDV_ECHANGE, "", request);
    }
}