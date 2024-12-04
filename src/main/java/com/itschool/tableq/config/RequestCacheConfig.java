package com.itschool.tableq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
public class RequestCacheConfig {

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache(); // 기본적으로 HttpSessionRequestCache 사용
    }
}