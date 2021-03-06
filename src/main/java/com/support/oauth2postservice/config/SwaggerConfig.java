package com.support.oauth2postservice.config;

import com.support.oauth2postservice.util.constant.UriConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class SwaggerConfig {

  @Bean
  public Docket postApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .consumes(getConsumeContentTypes())
        .produces(getProduceContentTypes())
        .apiInfo(getApiInfo())
        .groupName("posts")
        .select()
        .paths(PathSelectors.ant("/posts/**"))
        .build();
  }

  @Bean
  public Docket memberApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .consumes(getConsumeContentTypes())
        .produces(getProduceContentTypes())
        .apiInfo(getApiInfo())
        .groupName("members")
        .select()
        .paths(PathSelectors.ant("/members/**"))
        .build();
  }

  @Bean
  public Docket oauth2Api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .consumes(getConsumeContentTypes())
        .produces(getProduceContentTypes())
        .apiInfo(getApiInfo())
        .groupName("oauth2")
        .select()
        .paths(PathSelectors.ant("/oauth2/**"))
        .build();
  }

  private Set<String> getConsumeContentTypes() {
    return new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
  }

  private Set<String> getProduceContentTypes() {
    return new HashSet<>(Arrays.asList(MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE));
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
        .version("1.0")
        .title("OAUTH2-SUPPORT API")
        .description("[OAuth2-Support] API")
        .contact(new Contact("OAuth2 Support Swagger", UriConstants.Full.BASE_URL, "panda@gmail.com"))
        .build();
  }
}
