package com.daniel.springcloudfeignconsumer.service;

import com.daniel.springcloudfeignconsumer.fallback.HelloFallback;
import com.daniel.springcloudfeignconsumer.fallback.NewFallback;
import feign.Body;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "SPRING-CLOUD-SLEUTH-SERVER", fallback = NewFallback.class)
public interface NewService {

    @GetMapping(value = "/index")
    String index();
}
