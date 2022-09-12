package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 生产者分组
 *
 * 通过message.setStringProperty("JMSXGroupID", "GroupA")创建一个分组
 * 同一个组的消息，只会发送到同一个消费者，直到Consumer被关闭
 *
 * @author daniel
 */
public class MessageGroupProducer {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;

        try {
            //1.创建连接工厂
            String brokerUrl = "tcp://175.24.172.160:61616";//单体模式
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            //3.创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue = session.createQueue("foo.bar");
            //5.创建生产者消息
            MessageProducer producer = session.createProducer(queue);
            //6.设置生产者模式 队列数据清空或保存
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //7.消息发送
            for (int i = 0; i < 100; i++) {
                String text = "Hello World --- " + i + "!";
                TextMessage textMessage = session.createTextMessage(text);
                textMessage.setStringProperty("JMSXGroupID", "GroupA");
                if (i % 5 == 0) {
                    //将JMSXGroupSeq设置为-1后，GroupA将关闭，下次再发送GroupA的消息后，将会是另一个消费者接收
                    textMessage.setIntProperty("JMSXGroupSeq", -1);
                }
                producer.send(textMessage);

                textMessage = session.createTextMessage(text);
                textMessage.setStringProperty("JMSXGroupID", "GroupB");
                producer.send(textMessage);

                System.out.println("消息" + i + "已发送......");
            }
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
