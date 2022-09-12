package com.daniel.mqttspring;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.*;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

@SpringBootApplication
public class MqttSpringApplication {

    public static void main(String[] args) {
        /*
        * 示例：
        * https://spring.io/projects/spring-integration
        * https://gtihub.com/spring-projects/spring-integration-samples/
        */
        SpringApplication.run(MqttSpringApplication.class, args);
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new  String[]{"tcp://175.24.172.160:1883"});
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 发布 - 每秒发送hello mqtt到activemq
     */
    @Bean
    public IntegrationFlow mqttOutFlow() {
        return IntegrationFlows.from(() -> "hello mqtt", sourcePollingChannelAdapterSpec -> sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(1000))).transform(p -> p + " sent to MQTT").handle(mqttOutBound()).get();
    }

    @Bean
    public MessageHandler mqttOutBound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("client-si-producer-0", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("test");
        return messageHandler;
    }

    /**
     * 订阅
     */
    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(mqttInBound()).transform(p -> p + ", received from MQTT").handle(printHandler()).get();
    }

    public MessageHandler printHandler() {
        return message -> System.out.println(message.getPayload());

    }

    @Bean
    public MessageProducerSupport mqttInBound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("client-si-consumer-1", mqttClientFactory(), "test");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

}
