package com.tubeplus.board_service.adapter.web.config;

import com.tubeplus.board_service.adapter.web.controller.board.vo.BoardSearchType;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.PostingPageType;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BoardSearchType.ReqConverter());
        registry.addConverter(new PostingPageType.ReqConverter());
    }
}
