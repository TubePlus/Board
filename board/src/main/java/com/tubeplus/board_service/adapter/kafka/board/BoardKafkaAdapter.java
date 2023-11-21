package com.tubeplus.board_service.adapter.kafka.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tubeplus.board_service.application.board.domain.Board;
import com.tubeplus.board_service.application.board.port.out.BoardEventPublishable;
import com.tubeplus.board_service.adapter.kafka.common.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class BoardKafkaAdapter implements BoardEventPublishable {

    private final KafkaProducer kafkaProducer;

    @Value(" ${spring.kafka.topic1.name}")
    private String boardCreateTopic;


    @Override
    public void publishBoardCreated(Board madeBoard) {

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(madeBoard); //todo stateless한 함수라면 spring bean config 파일 작성해 생성자 주입
        } catch (Exception e) {
            e.printStackTrace();
        }

        kafkaProducer.sendMessage(boardCreateTopic, jsonInString);
    }


}
