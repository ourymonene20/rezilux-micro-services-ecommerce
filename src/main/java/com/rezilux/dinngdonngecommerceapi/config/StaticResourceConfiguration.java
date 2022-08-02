package com.rezilux.dinngdonngecommerceapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Value("${app.assets.path}")
    private String path;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("Mamadou Oury DIALLO" + path);
        registry
                .addResourceHandler("/assets/**")
                .addResourceLocations("file:" + path + "/");
    }
}
