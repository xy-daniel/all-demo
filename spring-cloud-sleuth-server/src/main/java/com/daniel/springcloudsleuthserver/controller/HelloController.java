package com.daniel.springcloudsleuthserver.controller;

import com.daniel.springcloudsleuthserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    UserService userService;

    @GetMapping("index")
    public String index() {
        userService.insert();
        userService.test();
        return "欢迎来到链路追踪sleuth的世界";
    }
}
