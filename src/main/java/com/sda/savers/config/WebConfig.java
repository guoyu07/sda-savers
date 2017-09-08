package com.sda.savers.config;

import com.sda.savers.web.interceptor.AuthenticationInterceptor;
import com.sda.savers.web.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Allen on 2017/8/25.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public HandlerInterceptor loggingInterceptor(){
        return new LoggingInterceptor();
    }
    @Bean
    public HandlerInterceptor authenticationInterceptor(){
        return new AuthenticationInterceptor();
    }

}
