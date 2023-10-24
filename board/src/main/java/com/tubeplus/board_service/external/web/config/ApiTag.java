package com.tubeplus.board_service.external.web.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@Tag(name = "custom annotation, RestController + RequestMapping + Swagger.Tag")
@RestController
@RequestMapping
public @interface ApiTag {

    // Spring
    @AliasFor(annotation = RequestMapping.class)
    String path();


    // Swagger
    @AliasFor(annotation = Tag.class)
    String name();

    @AliasFor(annotation = Tag.class)
    String description();

}
