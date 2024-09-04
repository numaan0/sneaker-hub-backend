package com.example.sneaker_hub_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profilePic/**")
                .addResourceLocations("file:D:/College/Capstone/sneaker-hub-backend/src/main/resources/static/profilePic/");
    }
}
