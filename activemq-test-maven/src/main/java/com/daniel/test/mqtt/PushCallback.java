package com.daniel.test.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PushCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("-----------");
        System.out.println(s);
        System.out.println(mqttMessage.toString());
        System.out.println("-----------");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("服务器是否正确接收:" + iMqttDeliveryToken.isComplete());
    }
}
