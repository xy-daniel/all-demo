package com.daniel.springcloudribbonconsumer.beans;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IRuleBean {


    @Bean
    public IRule ribbonRule() {
//        return new RandomRule();
        return new WeightedResponseTimeRule(); //随机数+总响应时长-平均响应时间
    }
}
