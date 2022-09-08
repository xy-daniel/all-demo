package com.daniel.activemqspringproduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;

/**
 * @author daniel
 */
@SpringBootApplication
public class ActivemqSpringProduceApplication {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() {
        jmsTemplate.convertAndSend("foo.bar", "Hello ActiveMQ!");
    }

    public static void main(String[] args) {
        SpringApplication.run(ActivemqSpringProduceApplication.class, args);
    }

}
