package com.daniel.simple;

import com.daniel.simple.converter.MyMessageConverter;
import com.daniel.simple.event.MyRemoteEvent;
import com.daniel.simple.sender.MySender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
@EnableBinding({Sink.class, Source.class})
public class SimpleStreamApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(SimpleStreamApplication.class, args);

        // -- bus --
        BusProperties busProperties = context.getBean(BusProperties.class);
        context.publishEvent(new MyRemoteEvent("this is a spring cloud bus message.", busProperties.getId()));
        // -- bus --

        Source source = context.getBean(Source.class);
        Thread.sleep(5000L);
        source.output().send(new GenericMessage<>("测试Source.class"));


        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        //注册到我们的spring上下文中
        annotationConfigApplicationContext.register(EventConfiguration.class);
        //启动上下文
        annotationConfigApplicationContext.refresh();

        //事件发布
        ((ApplicationEventPublisher) annotationConfigApplicationContext).publishEvent(new MyApplicationEvent("开始学习spring cloud bus"));

        //事件发布
        context.getBean(MySender.class).send("北京");
    }

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
    public MessageSource<String> sendMessage() {
        return () -> new GenericMessage<>("Hello Stream, I using integration send message with you!");
    }


    @Bean
    @StreamMessageConverter
    public MessageConverter customMessageConverter() {
        return new MyMessageConverter();
    }

    // ---------------------bus事件传播机制----------------------------

    /**
     * 自定义发布事件
     */
    public static class MyApplicationEvent extends ApplicationEvent {
        public MyApplicationEvent(String source) {
            super(source);
        }
    }

    /**
     * 自定义监听事件
     */
    @Configuration
    public static class EventConfiguration {

        @EventListener
        public void onEvent(MyApplicationEvent event) {
            System.out.println("监听到事件:" + event);
        }
    }
}
