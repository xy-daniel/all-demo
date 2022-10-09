package com.daniel.springcloudzkconsumer;

import com.daniel.springcloudzkconsumer.service.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class SpringCloudZkConsumerApplication {

    @Autowired
    Client client;

    @Value("${test}")
    String test;

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZkConsumerApplication.class, args);
    }

    @GetMapping("user")
    public String getUser(@RequestParam String name) {
        return client.getUser(name);
    }

    @GetMapping("test")
    public String test() {
        return test;
    }
}
