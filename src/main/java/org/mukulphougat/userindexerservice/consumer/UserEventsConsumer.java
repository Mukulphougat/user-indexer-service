package org.mukulphougat.userindexerservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.mukulphougat.userindexerservice.elastic.UserIndexer;
import org.mukulphougat.userindexerservice.model.UserRegisteredEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventsConsumer {

    private final ObjectMapper objectMapper;
    private final UserIndexer userIndexer;

    @KafkaListener(topics = "user-service-events", groupId = "user-indexer-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            System.out.println("== Kafka Message Received ==");
            System.out.println("Topic: " + record.topic());
            System.out.println("Partition: " + record.partition());
            System.out.println("Offset: " + record.offset());
            System.out.println("Key: " + record.key());
            System.out.println("Value: " + record.value());
            System.out.println("============================");

            UserRegisteredEvent event = objectMapper.readValue(record.value(), UserRegisteredEvent.class);
            userIndexer.index(event);
            System.out.println("Indexed user: " + event.getId());
        } catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }
}
