package com.daniel.springcloudeurekaproviderone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class SpringCloudEurekaProviderOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaProviderOneApplication.class, args);
    }

    @GetMapping("")
    public String index() {
        return "This is service1's response";
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
