package com.daniel.simple.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

public class MyEvent extends ContextRefreshedEvent {

    private final String to;

    public MyEvent(ApplicationContext source, String to) {
        super(source);
        this.to = to;
    }

    public String getTo() {
        return to;
    }
}
