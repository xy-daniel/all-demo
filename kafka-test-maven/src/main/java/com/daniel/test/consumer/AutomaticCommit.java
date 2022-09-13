package com.daniel.test.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class AutomaticCommit {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "175.24.172.160:9092");
        properties.setProperty("group.id", "test");

        properties.setProperty("enable.auto.commit", "true"); //自动提交
        /*
            commit:读取成功后要回写offset,此数据要存储在什么地方？一个topic:_consumer_offsets:topic+groupId+partition+offset

            什么时候提交？下一次poll的时候提交,可能会导致前一个数据重复消费

            提交的时候并没有提交进度,kafka是怎么知道offset的呢？kafka维护了一个内存变量fetch offset记录已经处理完成的偏移量
         */

        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("market_topic"));

        while (true) {
            System.out.println("开始了------");
            //下一次poll,会将上次poll的offset提交上去
            ConsumerRecords<String, String> records = consumer.poll(8000);
            for (ConsumerRecord<String, String> record : records) {
                long offset = record.offset();
                System.out.println("offset=" + offset);
                int partition = record.partition();
                System.out.println("partition=" + partition);
                String key = record.key();
                System.out.println("key=" + key);
                String value = record.value();
                System.out.println("value=" + value);
                long timestamp = record.timestamp();
                System.out.println("timestamp=" + timestamp);
            }
        }
    }
}
