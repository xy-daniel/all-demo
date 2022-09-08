package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.xml.soap.Text;

/**
 * 消费者
 * @author daniel
 */
public class Consumer {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;

        try {
            //1.创建连接工厂
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://175.24.172.160:61616");
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue = session.createQueue("foo.bar");
            //5.创建消费者消息
            consumer = session.createConsumer(queue);
            //6.接收消息
            Message message = consumer.receive(1000);
            if (message instanceof TextMessage) {
                System.out.println("消费者接收到文本信息：" + ((TextMessage)message).getText());
            } else {
                System.out.println(message);
            }
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
