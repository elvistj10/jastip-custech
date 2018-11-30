package com.cus.jastip.payment.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.cus.jastip.payment")
public class FeignConfiguration {

}
