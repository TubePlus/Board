package com.tubeplus.board_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // base entity 자동 적용
@SpringBootApplication
public class BoardApplication {
public static void main(String[] args) {

		SpringApplication.run(BoardApplication.class, args);
	}

}
