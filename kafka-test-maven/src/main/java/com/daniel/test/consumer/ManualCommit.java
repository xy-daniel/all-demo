package com.daniel.test.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ManualCommit {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "175.24.172.160:9092");
        properties.setProperty("group.id", "test");
        properties.setProperty("enable.auto.commit", "false");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("market_topic"));

        final int minBatchSize = 1000;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();

        while (true) {
            System.out.println("getting......");
            ConsumerRecords<String, String> records = consumer.poll(4000);

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("received msg:" + record.value());
                buffer.add(record);
            }

            if (buffer.size() >= minBatchSize) {
                insertToDB(buffer);  //模拟插入数据库

                System.out.println("======>>>offset commit...");
                //数据处理完成但是提交失败,会导致数据的重复消费
                //数据没有达到需要处理的阈值完成之后自动提交会导致丢数据
                consumer.commitSync();
                buffer.clear();
            }
        }
    }

    private static void insertToDB(List<ConsumerRecord<String, String>> buffer) {
        System.out.println("模拟插入数据库......");
    }
}
