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
    Set<String> consumes = new HashSet<>();
    consumes.add(MediaType.APPLICATION_JSON_VALUE);
    consumes.add(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    return consumes;
  }

  private Set<String> getProduceContentTypes() {
    Set<String> produces = new HashSet<>();
    produces.add(MediaType.APPLICATION_JSON_VALUE);
    return produces;
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
        .title("API")
        .version("1.0")
        .description("[OAuth2-Support] API")
        .contact(new Contact("OAuth2 Support Swagger", UriConstants.Full.BASE_URL, "panda@gmail.com"))
        .build();
  }
}
