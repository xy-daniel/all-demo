package com.daniel.test;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;

/**
 * @author adniel
 */
public class MqttConsumer {

    private static final int QOS = 2;
    private static final String BROKER_URL = "tcp://175.24.172.160:1883";
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient mqttClient = new MqttClient(BROKER_URL, "consumer-client-id-1", persistence, Executors.newScheduledThreadPool(10));
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(false);
        connectOptions.setUserName(USER_NAME);
        connectOptions.setPassword(PASSWORD.toCharArray());
        connectOptions.setKeepAliveInterval(20);
        mqttClient.connect(connectOptions);
        System.out.println("connect success");
        int[] qos = {QOS};
        String[] topics = {"foo.bar"};
        mqttClient.subscribe(topics, qos, new IMqttMessageListener[]{(s, mqttMessage) -> System.out.println("收到新消息：" + s + " > " + mqttMessage.toString())});
    }
}