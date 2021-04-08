package com.ataccama.databasebrowser.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class ApplicationSwaggerConfig {

    @Bean
    public Docket dbBrowserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ataccama.databasebrowser.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Database Browser API")
                .version("1.0")
                .description("API for managing connections to a DB and getting information from the DB.")
                .contact(new Contact("Luis E. López", "https://www.linkedin.com/in/luisemiliolopez/", "lemilio@gmail.com"))
                .license("Apache License Version 2.0")
                .build();
    }
}
