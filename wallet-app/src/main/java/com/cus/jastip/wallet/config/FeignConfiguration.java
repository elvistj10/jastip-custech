package com.cus.jastip.wallet.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.cus.jastip.wallet")
public class FeignConfiguration {

}
