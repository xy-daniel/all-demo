package com.daniel.springcloudeurekaconsumer.beans;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateBean {

    @Bean
    @LoadBalanced //负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
