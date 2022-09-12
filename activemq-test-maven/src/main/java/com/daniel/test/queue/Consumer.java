package com.daniel.test.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.xml.soap.Text;

/**
 * 简单消费者
 *
 * @author daniel
 */
public class Consumer {
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
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue = session.createQueue("foo.bar");
            //5.创建消费者消息
            consumer = session.createConsumer(queue);
            //6.接收消息
            while (true) {
                Message message = consumer.receive(1000);
                if (message instanceof TextMessage) {
                    System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                } else {
                    System.out.println(message);
                }
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
