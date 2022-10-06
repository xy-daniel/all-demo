package com.daniel.springcloudfeignconsumer.service;

import com.daniel.springcloudfeignconsumer.fallback.HelloFallback;
import feign.Body;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "haihaihai", fallback = HelloFallback.class)
public interface HelloService {

    @GetMapping(value = "/")
    String index();

    @GetMapping(value = "/user")
    String getName(@RequestParam("name") String name);

    @PostMapping(value = "/testName")
    String getName(@RequestParam("id") String id, @RequestParam("name") String name);

    @PostMapping(value = "/test", consumes = "application/json;charset=UTF-8")
    @Body("{body}")
    String test(@Param("body") String body);
}
