package com.gwu.system.config;

import static org.springframework.web.cors.CorsConfiguration.ALL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** 
 * 说明：跨越请求处理
 * 创建人：gwu Q402920025
 * 修改时间：2019年03月05日
 * @version 1.0
 */
@Configuration
public class CORSConfig {

    @SuppressWarnings("deprecation")
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(ALL)
                        .allowedMethods(ALL)
                        .allowedHeaders(ALL)
                        .allowCredentials(true);
            }
        };
    }

}