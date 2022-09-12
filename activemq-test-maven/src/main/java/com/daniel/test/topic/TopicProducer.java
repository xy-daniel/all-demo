package com.daniel.test.topic;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicProducer {

    public static void main(String[] args) {
        ProducerThread producerThread = new ProducerThread("tcp://175.24.172.160:61616", "test");
        producerThread.start();
    }

    static class ProducerThread extends Thread {
        String brokerUrl;
        String destinationUrl;

        public ProducerThread(String brokerUrl, String destinationUrl) {
            this.brokerUrl = brokerUrl;
            this.destinationUrl = destinationUrl;
        }

        @Override
        public void run() {
            ActiveMQConnectionFactory connectionFactory;
            Connection connection = null;
            Session session;

            try {
                System.out.println("线程开始运行......");
                connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
                connection = connectionFactory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic(destinationUrl);
                //5.创建生产者消息
                MessageProducer producer = session.createProducer(topic);
                //6.设置生产者模式 队列数据清空或保存
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                //7.发送消息
                for (int i = 0; i < 100; i++) {
                    String text = "Hello World --- " + i + "!";
                    TextMessage textMessage = session.createTextMessage(text);
                    producer.send(textMessage);
                    System.out.println("消息发送成功......");
                }
            } catch (JMSException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.stop();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
