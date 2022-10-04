package com.daniel.springcloudeurekaprovidertwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
@RefreshScope
public class SpringCloudEurekaProviderTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaProviderTwoApplication.class, args);
    }

    @GetMapping("")
    public Object index() {
        return "This is service2's response!";
    }



}
