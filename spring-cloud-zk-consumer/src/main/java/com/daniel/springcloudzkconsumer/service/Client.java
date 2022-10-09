package com.daniel.springcloudzkconsumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "spring-cloud-zk-provider")
public interface Client {

    @GetMapping("user")
    String getUser(@RequestParam("name") String name);
}
