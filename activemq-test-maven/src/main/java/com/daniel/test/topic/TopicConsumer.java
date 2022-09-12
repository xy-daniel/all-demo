package com.daniel.test.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 非持久订阅者
 * 非持久订阅者只有当客户端处于连接状态才能收到发送到某个主题的消息
 * 而当客户端处于离线状态，这个时间段发送到主题的消息永远不会收到
 */
public class TopicConsumer {
    public static void main(String[] args) {
        new ConsumerThread("tcp://175.24.172.160:61616", "test").start();
        new ConsumerThread("tcp://175.24.172.160:61616", "test").start();
    }

    static class ConsumerThread extends Thread {

        String brokerUrl;
        String destination;

        public ConsumerThread(String brokerUrl, String destination) {
            this.brokerUrl = brokerUrl;
            this.destination = destination;
        }

        @Override
        public void run() {
            ActiveMQConnectionFactory connectionFactory;
            Connection connection = null;
            Session session;
            MessageConsumer consumer;

            try {
                System.out.println("线程开始运行......");
                //1.创建连接工厂
                connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
                //2.创建连接
                connection = connectionFactory.createConnection();
                connection.start();
                //3.创建会话
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //4.创建点对点发送的目标
                Topic topic = session.createTopic(destination);
                //5.创建消费者消息
                consumer = session.createConsumer(topic);
                //6.接收消息
                while (true) {
                    Message message = consumer.receive(1000);
                    if (message instanceof TextMessage) {
                        System.out.println("消费者接收到文本信息：" + ((TextMessage)message).getText());
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
}


