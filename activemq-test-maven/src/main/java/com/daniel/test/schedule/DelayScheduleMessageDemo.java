package com.daniel.test.schedule;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

/**
 * 延时调度消息：http://activemq.apache.org/delay-and-schedule-message-delivery.html
 * 定时发送邮件通知，或者触发代码执行
 */
public class DelayScheduleMessageDemo {
    public static void main(String[] args) {
        new ProducerThread("tcp://175.24.172.160:61616", "foo.bar").start();
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
            ActiveMQConnectionFactory activeMQConnectionFactory;
            Connection connection = null;
            Session session;

            try {
                activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
                connection = activeMQConnectionFactory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Queue queue = session.createQueue(destinationUrl);
                MessageProducer producer = session.createProducer(queue);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                //示例消息
                //延迟5秒
                TextMessage textMessage1 = session.createTextMessage("Hello World - 1");
                textMessage1.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5 * 1000L);
                //延迟5秒投递3次，间隔10秒(投递次数=重复次数+默认的一次)
                TextMessage textMessage2 = session.createTextMessage("Hello World - 2");
                textMessage2.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5 * 1000L);
                textMessage2.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 10 * 1000L);
                textMessage2.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 2);
                //CRON表达式的方式以及和上面参数的组合
                TextMessage textMessage3 = session.createTextMessage("Hello World - 3");
                textMessage3.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "0/5 * * * *");

                producer.send(textMessage1);
                producer.send(textMessage2);
                producer.send(textMessage3);
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
