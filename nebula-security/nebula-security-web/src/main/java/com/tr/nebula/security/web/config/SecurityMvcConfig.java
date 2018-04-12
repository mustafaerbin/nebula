package com.tr.nebula.security.web.config;

import com.tr.nebula.security.web.handler.NebulaSessionUserHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by Mustafa Erbin on 20.04.2017.
 */
@Configuration
@EnableWebMvc
public class SecurityMvcConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new NebulaSessionUserHandler());
    }
}
