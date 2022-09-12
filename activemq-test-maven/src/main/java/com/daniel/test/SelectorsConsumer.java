package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

/**
 * 消息过滤
 *
 * 消费者在接收到消息时，支持根据消息属性进行条件过滤，条件为字符串，语法和SQL的where条件类似
 *
 * @author daniel
 */
public class SelectorsConsumer {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable1 = () -> receive("age >= 18 and gender = '女'");
        Runnable runnable2 = () -> receive("gender = '男'");
        new Thread(runnable1).start();
        new Thread(runnable2).start();
        System.out.println("消费者启动完成");
        Thread.sleep(5000L);
        new Thread(SelectorsConsumer::send).start();
        System.out.println("生产者启动完成");
    }

    private static Session connect() throws JMSException {
        String userName = "admin";
        String password = "admin";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://175.24.172.160:61616");
        Connection connection = connectionFactory.createConnection(userName, password);
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private static void send() {
        try {
            Session session = connect();
            //复合消息
            Queue queue = session.createQueue("foo.bar, topic://for.bar");
            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            for (int i = 0; i < 100; i++) {
                String text = i + ".Hello HaHaHa......";
                TextMessage textMessage = session.createTextMessage(text);
                textMessage.setIntProperty("age", new Random().nextInt(30));
                textMessage.setStringProperty("gender", i % 2 == 0 ? "男" : "女");
                producer.send(textMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static void receive(String conditions) {
        try {
            Session session = connect();
            Queue queue = session.createQueue("foo.bar");
            MessageConsumer consumer = session.createConsumer(queue, conditions);
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                        System.out.println("age:" + message.getIntProperty("age"));
                        System.out.println("gender:" + message.getStringProperty("gender"));
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(message);
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
