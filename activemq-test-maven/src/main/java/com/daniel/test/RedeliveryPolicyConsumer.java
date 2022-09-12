package com.daniel.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.io.IOException;

/**
 * 消息重发消费者
 *
 * @author daniel
 */
public class RedeliveryPolicyConsumer {
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;

        try {
            //创建队列重复策略
            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            redeliveryPolicy.setInitialRedeliveryDelay(0);  //出事重发延迟时间，单位毫秒
            redeliveryPolicy.setRedeliveryDelay(5000);  //第一次以后的延迟时间
            redeliveryPolicy.setUseExponentialBackOff(false);  //是否以指数递增的方式增加超时时间
            redeliveryPolicy.setMaximumRedeliveries(3);  //最大重发次数,从0开始计数,为-1则不适用最大次数

            // 1.创建连接工厂
            // 单体模式
            String brokerUrl = "tcp://175.24.172.160:61616";
            // 集群模式 - https://activemq.apache.org/failove-transport-reference.html
            // randomize=false:随机选择，默认是顺序
            // priorityBackup=true&priorityURIs=tcp:175.24.172.160:61616:指定有限切换
            // maxReconnectDelay重连的最大间隔时间(毫秒)
            //String brokerUrl = "failover:(tcp://175.24.172.160:61616,tcp://175.24.172.160:61616,tcp://175.24.172.160:61616)?initialReconnectDelay=100";
            connectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
            connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
            //2.创建连接
            connection = connectionFactory.createConnection();
            connection.start();
            //3.创建会话 - 确认模式设置为客户端手动确认
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            //4.创建点对点发送的目标
            Queue queue = session.createQueue("foo.bar");
            //5.创建消费者消息
            consumer = session.createConsumer(queue);
            //6.接收消息
            Session finalSession = session;
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (((TextMessage) message).getText().endsWith("3!")) {
                            throw new RuntimeException("消息重发");
                        }
                        System.out.println("消费者接收到文本信息：" + ((TextMessage) message).getText());
                        //确认收到消息
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        try {
                            //消息重发
                            finalSession.recover();
                        } catch (JMSException jmsException) {
                            jmsException.printStackTrace();
                        }
                    }
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
