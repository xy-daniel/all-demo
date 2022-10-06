package com.daniel.springcloudfeignconsumer.controller;

import com.daniel.springcloudfeignconsumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    HelloService helloService;

    @GetMapping("index")
    public Object getIndex() {
        return helloService.index();
    }

    @GetMapping("getName")
    public Object getName(@RequestParam("name") String name) {
        return helloService.getName(name);
    }

    @GetMapping("/testName")
    public String testUser() {
        return helloService.getName("1","daniel");
    }

    @GetMapping("/test")
    public String test() {
        return helloService.test("{\"name\":\"daniel\"}");
    }
}
