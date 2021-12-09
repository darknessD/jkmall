package com.jkmall.order.config;

import com.jchen.entity.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IDGeneratorConfig {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
