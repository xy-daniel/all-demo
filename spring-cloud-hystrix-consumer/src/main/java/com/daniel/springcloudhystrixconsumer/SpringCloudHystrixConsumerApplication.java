package com.daniel.springcloudhystrixconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

@SpringBootApplication
@EnableCircuitBreaker
public class SpringCloudHystrixConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixConsumerApplication.class, args);
    }

}
