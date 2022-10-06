package com.daniel.springcloudeurekaprovidertwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

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
    public String index() throws InterruptedException {
        Random random = new Random();
        int times = random.nextInt(150);
        System.out.println("超时时间:" + times);
        Thread.sleep(times);
        return "This is service2's response!";
    }

    @GetMapping("user")
    public String getName(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @PostMapping("testName")
    public String testName(@RequestParam("id") String id, @RequestParam("name") String name) {
        return id + ":" + name;
    }

    @PostMapping("test")
    public String testName(@RequestBody Map<String, Object> requestData) {
        return requestData.toString();
    }
}
