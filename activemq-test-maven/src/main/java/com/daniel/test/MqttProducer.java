package com.daniel.test;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.jms.TextMessage;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * @author adniel
 */
public class MqttProducer {

    private static final int QOS = 2;
    private static final String BROKER_URL = "tcp://175.24.172.160:1883";
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(false);
        connectOptions.setUserName(USER_NAME);
        connectOptions.setPassword(PASSWORD.toCharArray());
        connectOptions.setKeepAliveInterval(20);
        MqttClient mqttClient = new MqttClient(BROKER_URL, "producer-client-id-1", persistence, Executors.newScheduledThreadPool(10));
        mqttClient.setCallback(new PushCallback());
        mqttClient.connect(connectOptions);

        MqttMessage mqttMessage = new MqttMessage("哈哈哈".getBytes(StandardCharsets.UTF_8));
        mqttMessage.setQos(1);
        mqttMessage.setRetained(false);
        mqttClient.publish("foo.bar", mqttMessage);

        mqttClient.disconnect();
    }


}