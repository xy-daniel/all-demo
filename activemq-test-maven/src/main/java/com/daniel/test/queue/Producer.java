package com.daniel.test.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 简单生产者
 * @author daniel
 */
public class Producer {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;

        try {
            //1.创建连接工厂
            String brokerUrl = "tcp://175.24.172.160:61616";//单体模式
//            String brokerUrl = "failover:(tcp://175.24.172.160:61616,tcp://175.24.172.160:61616,tcp://175.24.172.160:61616)?initialReconnectDelay=100";//集群模式
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
//            Queue queue = session.createQueue("x/y/z");
//            //4.创建爱你发布的目标
            Topic topic = session.createTopic("x/y/z");
            //5.创建生产者消息
            MessageProducer producer = session.createProducer(topic);
            //6.设置生产者模式 队列数据清空或保存
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //7.创建一条消息
            String text = "Hello World!";
            TextMessage textMessage = session.createTextMessage(text);
            //8.发送消息
            producer.send(textMessage);
        } catch (Exception e) {
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
