package com.daniel.springcloudribbonconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

@SpringBootApplication
@RibbonClients(
        @RibbonClient(value = "hello-server")
)
public class SpringCloudRibbonConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudRibbonConsumerApplication.class, args);
    }

}
