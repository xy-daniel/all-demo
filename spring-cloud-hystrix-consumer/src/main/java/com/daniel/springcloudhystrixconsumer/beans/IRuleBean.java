package com.daniel.springcloudhystrixconsumer.beans;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IRuleBean {

    @Bean
    public IRule iRule() {
        return new RoundRobinRule();
    }
}
