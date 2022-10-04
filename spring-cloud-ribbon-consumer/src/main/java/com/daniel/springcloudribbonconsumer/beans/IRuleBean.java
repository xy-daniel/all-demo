package com.daniel.springcloudribbonconsumer.beans;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IRuleBean {


    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }
}
