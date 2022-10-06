package com.daniel.definedfegin.service;

import com.daniel.definedfegin.annotation.FeignClient;
import com.daniel.definedfegin.annotation.FeignGet;
import org.springframework.stereotype.Component;

@Component
@FeignClient(baseUrl = "http://127.0.0.1:8001")
public interface TestFeign {

    @FeignGet(url = "")
    Object getSomeThing();
}
