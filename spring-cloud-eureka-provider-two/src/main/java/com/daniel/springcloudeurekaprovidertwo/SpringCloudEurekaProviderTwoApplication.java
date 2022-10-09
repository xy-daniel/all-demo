package com.daniel.springcloudeurekaprovidertwo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
@RefreshScope //自动刷新远程配置
public class SpringCloudEurekaProviderTwoApplication {

    @Value("${user.name}")
    private String name;

    @Value("${user.password}")
    private String password;

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

    @GetMapping("refresh/getSet")
    public Map<String, String> getSet() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("password", password);
        return hashMap;
    }
}
