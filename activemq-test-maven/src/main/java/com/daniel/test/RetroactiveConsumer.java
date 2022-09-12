package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息追溯
 * 使消费者可以接收到历史的topic消息,需要activemq.xml中给主题设置恢复策略配置
 *  http://activemq.apache.org/subscription-recovery-polivy.html
 *  <subscriptionRecoveryPolicy></subscriptionRecoveryPolicy>
 *
 * @author daniel
 */
public class RetroactiveConsumer {
    public static void main(String[] args) {
        Runnable runnable = RetroactiveConsumer::receive;
        new Thread(runnable).start();
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
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Topic topic = session.createTopic("test");
            //5.创建消费者消息
            consumer = session.createConsumer(topic);
            //6.接收消息
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(message);
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
