package com.daniel.simple.sender;

import com.daniel.simple.event.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MySender {

    @Autowired
    private ApplicationContext applicationContext;

    public void send(String to) {
        System.out.println("开始发布事件");
        applicationContext.publishEvent(new MyEvent(applicationContext, to));
    }
}
