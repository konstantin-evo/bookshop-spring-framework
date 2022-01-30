package com.example.bookshop.app.config;

import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookDto;
import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringfoxConfig {

    private final TypeResolver typeResolver;

    public SpringfoxConfig(final TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .additionalModels(
                        typeResolver.resolve(BookDto.class),
                        typeResolver.resolve(AuthorDto.class))
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo() {
        return new ApiInfo(
                "Bookshop API",
                "The Book API is a generic, flexible, configurable endpoint which allows requesting information on one or more books.",
                "1.0",
                "http://www.termsofservice.org",
                new Contact("Konstantin Priluchnyi", "https://github.com/konstantin-evo",
                        "konstantin.priluchnyi@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }
}
