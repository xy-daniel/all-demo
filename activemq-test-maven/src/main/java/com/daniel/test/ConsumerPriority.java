package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 消费者优先级1-127
 *
 * @author daniel
 */
public class ConsumerPriority {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;

        try {
            // 1.创建连接工厂
            String brokerUrl = "tcp://175.24.172.160:61616";
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue;
            int read = System.in.read();
            if (read == 0) {
                //消费者开启了独占模式，即使其他消费者的优先级更改都不会再收到消息
                queue = session.createQueue("foo.bar?consumer.priority=0&consumer.exclusive=true");
            } else {
                queue = session.createQueue("foo.bar?consumer.priority=20");
            }
            //5.创建消费者消息
            consumer = session.createConsumer(queue);
            //6.接收消息
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        try {
                            System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(message);
                    }
                }
            });
            int read1 = System.in.read();
            System.out.println(read1);
        } catch (JMSException | IOException e) {
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
