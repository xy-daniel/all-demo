package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者分组
 *
 * 同一个组的消费组不会对同一个消息重复消费
 *
 * @author daniel
 */
public class MessageGroupConsumer {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                receive();
            }
        };
        new Thread(runnable, "C1").start();
        new Thread(runnable, "C2").start();
        new Thread(runnable, "C3").start();
    }

    public static void receive() {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;
        String brokerUrl = "tcp://175.24.172.160:61616";
        String userName = "admin";
        String password = "admin";

        try {
            // 1.创建连接工厂
            connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            //2.创建连接
            connection = connectionFactory.createConnection(userName, password);
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue = session.createQueue("foo.bar");
            //5.创建消费者消息
            consumer = session.createConsumer(queue);
            //6.接收消息
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                            System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                    } else {
                        System.out.println(message);
                    }
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
