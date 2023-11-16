package com.tubeplus.board_service.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String kafkaTopic, String message){
        System.out.println("kafka message send:" + message);
        try{
            kafkaTopic = kafkaTopic.trim();
            kafkaTemplate.send(kafkaTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
