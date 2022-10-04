package com.daniel.springcloudribbonretryconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("index")
    public Object getIndex() {
        return restTemplate.getForObject("http://hello-server", String.class, "");
    }

}
