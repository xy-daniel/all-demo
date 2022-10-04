package com.daniel.springcloudeurekaproviderone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class SpringCloudEurekaProviderOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaProviderOneApplication.class, args);
    }

    @GetMapping("")
    public Object index() {
        return "This is service1's response";
    }

}
