package com.wzn.azblog.Statistics;

import com.wzn.ablog.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class})
@EntityScan("com.wzn.ablog.common.entity")
@EnableFeignClients
public class StatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
