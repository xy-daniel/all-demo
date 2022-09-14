package com.daniel.test.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class AutomaticCommit {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "175.24.172.160:9092");
        properties.setProperty("group.id", "test");

        properties.setProperty("enable.auto.commit", "true"); //自动提交
        properties.setProperty("auto.commit.interval.ms", "1000");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("test"));

        while (true) {
            System.out.println("开始了------");
            //下一次poll,会将上次poll的offset提交上去
            ConsumerRecords<String, String> records = consumer.poll(5000);
            System.out.println(records.count());
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
