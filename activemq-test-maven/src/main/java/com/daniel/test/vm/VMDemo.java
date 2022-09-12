package com.daniel.test.vm;

import org.apache.activemq.broker.BrokerService;

/**
 * 嵌入式的，或者自身系统中实现队列
 */
public class VMDemo {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                BrokerService brokerService = new BrokerService();
                brokerService.setBrokerName("com.daniel.test.vm-activemq");
                brokerService.addConnector("tcp://127.0.0.1:61616");
                brokerService.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
