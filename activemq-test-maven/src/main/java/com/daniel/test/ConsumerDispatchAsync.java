package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 消费者异步调度
 * 从ActiveMQ v4开始,消费者异步调度的额配置更加灵活，可以再连接URI，Connect和ConnectionFactory上进行配置
 * 可以再broker的配置中，通过disableAsyncDispatch属性禁用transportConnector上的异步调度，禁用这个输出连接后，在客户端将无法开启
 * <transportConnector name="openwire" uri="tcp://0.0.0.0:61616" disableAsyncDispatch="true"/>
 *
 * 通过这种灵活的配置，可以实现为较慢的消费者提供异步消息传递，而为较快哦的消费者提供同步消息传递
 * 使用同步消息的缺点是如果向较慢的消费者发送消息时，可能造成生产者阻塞
 *
 * @author daniel
 */
public class ConsumerDispatchAsync {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;

        try {
            // 1.创建连接工厂
            String brokerUrl = "tcp://175.24.172.160:61616";
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
//            connectionFactory.setDispatchAsync(true);
            //2.创建连接
            connection = connectionFactory.createConnection();
//            ((ActiveMQConnection)connection).setDispatchAsync(true);
            connection.start();
            //3.创建会话 - 确认模式设置为客户端手动确认
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue = session.createQueue("foo.bar?consumer.dispatchAsync=false");
            //5.创建永久订阅者
            consumer = session.createConsumer(queue);
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
