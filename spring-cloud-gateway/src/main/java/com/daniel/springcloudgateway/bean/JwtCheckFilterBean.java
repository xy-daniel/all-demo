package com.daniel.springcloudgateway.bean;

import com.daniel.springcloudgateway.filter.JwtCheckGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JwtCheckFilterBean {

    @Bean
    public JwtCheckGatewayFilterFactory filterFactory() {
        return new JwtCheckGatewayFilterFactory();
    }
}
