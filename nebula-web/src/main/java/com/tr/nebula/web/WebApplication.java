package com.tr.nebula.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by kamilbukum on 28/02/2017.
 */
@SpringBootApplication
@EnableSwagger2
public class WebApplication extends SpringBootServletInitializer implements ApplicationRunner {

    @Value("${nebula.value:init}")
    private String initKey;

    private static ConfigurableApplicationContext context;

    public static ConfigurableApplicationContext run(Class<?> clazz, String[] args) {
        context = SpringApplication.run(new Class[]{clazz}, args);
        return context;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass()).bannerMode(Banner.Mode.OFF);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (applicationArguments.containsOption(initKey)) {
            init(applicationArguments);
            System.exit(0);
        }
    }

    public void init(ApplicationArguments applicationArguments) {

    }

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Springfox API")
                .description("Nebula")
                .contact(new Contact("admin", "http://www.nebula.com", "admin"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }

}
