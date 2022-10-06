package com.daniel.definedfegin.controller;

import com.daniel.definedfegin.service.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    TestFeign testFeign;

    @GetMapping("index")
    public Object getIndex() {
        return testFeign.getSomeThing();
    }
}
