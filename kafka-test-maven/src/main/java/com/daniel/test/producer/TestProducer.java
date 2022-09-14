package com.daniel.test.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;

import java.util.Properties;

public class TestProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "175.24.172.160:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("partitioner.class", MyPartitioner.class.getName());
        props.put("partitioner.class", DefaultPartitioner.class.getName());
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 10; i++) {
            System.out.println("start send......");
            producer.send(new ProducerRecord<>("test", "" + i, "message" + i));
            System.out.println("success send......");
        }
        //Thread.sleep (1000L) ;
        producer.close();
        System.out.println("done...");
    }
}


