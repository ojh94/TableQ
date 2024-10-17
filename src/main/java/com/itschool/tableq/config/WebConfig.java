package com.itschool.tableq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class WebConfig {

    @Bean
    public View jsonView() {
        return new MappingJackson2JsonView();
    }
}

