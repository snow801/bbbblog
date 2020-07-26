package com.wzn.ablog.admin;

import com.wzn.ablog.admin.config.AdminRsaKeyConfig;
import com.wzn.ablog.common.utils.IdWorker;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(AdminRsaKeyConfig.class)
@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.wzn.ablog.common.entity")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
    }

    @Bean
    public AdminRsaKeyConfig adminRsaKeyConfig(){
        return new AdminRsaKeyConfig();
    }

    @Bean
    public RsaKeyConfig rsaKeyConfig(){
        return new RsaKeyConfig();
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
