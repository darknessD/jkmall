package com.jkmall.seckill;


import com.jchen.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.jkmall.seckill.feign"})
@MapperScan(basePackages = {"com.jkmall.seckill.dao"})
@EnableScheduling
@EnableAsync
public class SecKillApplication {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class, args);
    }
}
