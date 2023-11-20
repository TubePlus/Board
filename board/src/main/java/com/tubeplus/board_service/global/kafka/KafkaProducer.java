package com.tubeplus.board_service.global.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tubeplus.board_service.global.kafka.dto.CommunityInteractionDto;
import com.tubeplus.board_service.global.kafka.dto.InteractionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.kafka.topic3.name}")
    private String topic3;

    public void sendMessage(String kafkaTopic, String message){
        System.out.println("kafka message send:" + message);
        try{
            kafkaTopic = kafkaTopic.trim();
            kafkaTemplate.send(kafkaTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void producerPutComment(Long communityId, Long point) throws JsonProcessingException {
        CommunityInteractionDto communityInteractionDto
                = new CommunityInteractionDto(communityId, point, InteractionType.COMMENT);
        // communityInteractionDto를 json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInString = objectMapper.writeValueAsString(communityInteractionDto);
        sendMessage(topic3,jsonInString);
    }
    public void producerLikePosting(Long communityId, Long point) throws JsonProcessingException {
        CommunityInteractionDto communityInteractionDto
                = new CommunityInteractionDto(communityId, point, InteractionType.LIKE);
        // communityInteractionDto를 json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInString = objectMapper.writeValueAsString(communityInteractionDto);
        sendMessage(topic3,jsonInString);
    }
}
