package com.wzn.ablog.article;

import com.wzn.ablog.common.utils.IdWorker;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

//exclude排除security自动认证
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class})
@EnableEurekaClient
@EnableConfigurationProperties(RsaKeyConfig.class)
@EnableFeignClients(basePackages = {"com.wzn.ablog.article.feign"})
@EnableCircuitBreaker//hystrix
@EnableHystrixDashboard//hystrix页面监控平台
@EntityScan("com.wzn.ablog.common.entity")
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

}
