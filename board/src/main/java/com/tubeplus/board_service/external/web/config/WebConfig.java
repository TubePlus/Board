package com.tubeplus.board_service.external.web.config;

import com.tubeplus.board_service.external.web.driving_adapter.board.vo.BoardSearchType;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BoardSearchType.BoardSearchTypeConverter());
    }
}
