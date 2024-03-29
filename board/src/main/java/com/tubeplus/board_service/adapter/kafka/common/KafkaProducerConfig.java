package com.tubeplus.board_service.adapter.kafka.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    @Value(" ${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;
    @Value(" ${spring.kafka.topic1.name}")
    private String topic1;
    @Value(" ${spring.kafka.topic2.name}")
    private String topic2;
    @Value(" ${spring.kafka.topic3.name}")
    private String topic3;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        kafka security
//        config.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"tubeplus\" password=\"2tube2!\";");
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public NewTopic Topic1() {
        return new NewTopic(topic1, 1, (short) 1);
    }
    @Bean
    public NewTopic Topic2() {
        return new NewTopic(topic2, 1, (short) 1);
    }
    @Bean
    public NewTopic Topic3() {
        return new NewTopic(topic3, 3, (short) 3);
    }
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
