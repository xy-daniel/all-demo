package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.io.IOException;

/**
 * 永久订阅者 - 可以使用虚拟目标VTC实现高可用
 * 持久订阅时，客户端向JMS注册一个自己身份的ID(clientId必须有)
 * 当这个客户端处于离线时，JMS Provider会为这个ID保存所有发送到主题的消息
 * 当客户端再次连接到JMS Provider时，会根据自己的ID得到当自己处于离线时发送到主题的消息
 *
 * @author daniel
 */
public class DurableSubscriber {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;

        try {
            // 1.创建连接工厂
            // 单体模式
            String brokerUrl = "tcp://175.24.172.160:61616";
            // 集群模式 - https://activemq.apache.org/failove-transport-reference.html
            // randomize=false:随机选择，默认是顺序
            // priorityBackup=true&priorityURIs=tcp:175.24.172.160:61616:指定有限切换
            // maxReconnectDelay重连的最大间隔时间(毫秒)
            //String brokerUrl = "failover:(tcp://175.24.172.160:61616,tcp://175.24.172.160:61616,tcp://175.24.172.160:61616)?initialReconnectDelay=100";
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.setClientID("test");
            connection.start();
            //3.创建会话 - 确认模式设置为客户端手动确认
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Topic topic = session.createTopic("test");
            //5.创建永久订阅者
            consumer = session.createDurableSubscriber(topic, "xxx");
            //6.接收消息
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof  TextMessage) {
                        System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                    } else {
                        System.out.println(message);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
            int read = System.in.read();
            System.out.println(read);
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
