package com.cus.jastip.transaction.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.cus.jastip.transaction")
public class FeignConfiguration {

}
