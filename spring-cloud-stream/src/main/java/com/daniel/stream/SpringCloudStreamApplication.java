package com.daniel.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
@EnableBinding(SpringCloudStreamApplication.TestOutput.class)
public class SpringCloudStreamApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringCloudStreamApplication.class, args);
        TestOutput output = context.getBean(TestOutput.class);
        output.output().send(new GenericMessage<>("11111"));
    }

    public interface TestOutput {

        @Output("testOutput")
        MessageChannel output();

    }

}
