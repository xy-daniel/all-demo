package com.daniel.test.vm;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerAndProducerVM {

    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer;

        try {
            System.out.println("线程开始运行......");
            //1.创建连接工厂
            connectionFactory = new ActiveMQConnectionFactory("com.daniel.test.vm:127.0.0.1");
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Topic topic = session.createTopic("test");
            //5.创建生产者消息
            MessageProducer producer = session.createProducer(topic);
            //6.设置生产者模式 队列数据清空或保存
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //7.创建消费者消息
            consumer = session.createConsumer(topic);
            while (true) {
                //8.创建一条消息
                String text = "Hello World!";
                TextMessage textMessage = session.createTextMessage(text);
                producer.send(textMessage);
                System.out.println("消息发送成功......");
                //9.接受一条消息
                Message message = consumer.receive(1000);
                if (message instanceof TextMessage) {
                    System.out.println("消费者接收到文本信息：" + ((TextMessage)message).getText());
                } else {
                    System.out.println(message);
                }
                Thread.sleep(1000L);
            }

        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    assert session != null;
                    session.close();
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
