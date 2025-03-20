package com.cretf.backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*") // Chấp nhận mọi domain
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép tất cả phương thức
                        .allowedHeaders("*") // Cho phép tất cả headers
                        .allowCredentials(true);
            }
        };
    }
}
