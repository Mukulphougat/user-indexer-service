package org.mukulphougat.userindexerservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    // No custom config yet — Spring Boot auto config is used
}
