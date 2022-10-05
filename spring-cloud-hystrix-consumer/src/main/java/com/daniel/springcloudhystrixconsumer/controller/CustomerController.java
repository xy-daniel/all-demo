package com.daniel.springcloudhystrixconsumer.controller;

import com.daniel.springcloudhystrixconsumer.command.CustomerCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("index")
    public Object getIndex() {
        return new CustomerCommand(restTemplate).execute();
    }

    @HystrixCommand(
            fallbackMethod = "callTimeoutFallback",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "1")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
            }
    )
    @GetMapping("test")
    public Object getTest() {
        return restTemplate.getForObject("http://spring-cloud-eureka-provider", String.class, "");
    }

    public Object callTimeoutFallback() {
        return "请求Test降级";
    }
}
