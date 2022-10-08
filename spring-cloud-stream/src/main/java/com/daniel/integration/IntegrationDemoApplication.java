package com.daniel.integration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
public class IntegrationDemoApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(IntegrationDemoApplication.class, args);

        Thread.sleep(5000L);
        MessageChannel outboundChannel = (MessageChannel) applicationContext.getBean("amqpOutputChannel");
        MessagingTemplate template = new MessagingTemplate();
        template.sendAndReceive(outboundChannel, new GenericMessage<>("我来自amqpOutboundChannel"));

    }

    @Bean
    public MessageChannel amqpInputChannel() {
        //为每个发送消息调用的单个订阅通道,调用是发生在发送方的线程中
        return new DirectChannel();
    }

    @Bean
    public AmqpInboundChannelAdapter inbound(ConnectionFactory factory, @Qualifier("amqpInputChannel") MessageChannel messageChannel) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setQueueNames("queue-1");
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(container);
        adapter.setOutputChannel(messageChannel);
        return adapter;
    }

    // 模拟在其他系统发送消息

    @Bean
    public MessageChannel amqpOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "amqpOutputChannel")
    public AmqpOutboundEndpoint outbound(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
        outbound.setRoutingKey("queue-1");
        return outbound;
    }


}
