package cn.yunshi.groupcontrol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author: xietao
 * @Date: 2023-10-24 16:37
 **/
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

}
