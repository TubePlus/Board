package com.tubeplus.board_service.adapter.web.config;

import com.tubeplus.board_service.adapter.web.controller.board.vo.BoardSearchType;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.PostingsSearchTypeReq;
import com.tubeplus.board_service.adapter.web.controller.posting.vo.posting.PostingsViewTypeReq;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // CORS를 적용할 URL 패턴 정의
                .allowedOrigins("*") // 허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 허용할 HTTP method
                .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }

    protected void addBoardFormatters(FormatterRegistry registry) {

        registry.addConverter(new BoardSearchType.ReqConverter());
    }

    protected void addPostingFormatters(FormatterRegistry registry) {

        registry.addConverter(new PostingsViewTypeReq.ReqConverter());
        registry.addConverter(new PostingsSearchTypeReq.ReqConverter());
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        this.addBoardFormatters(registry);
        this.addPostingFormatters(registry);
    }
}
