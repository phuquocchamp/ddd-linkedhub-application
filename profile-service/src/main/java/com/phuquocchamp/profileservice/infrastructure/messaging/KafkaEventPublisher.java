package com.phuquocchamp.profileservice.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Object event){
        kafkaTemplate.send("profile-events", event);
    }
}
