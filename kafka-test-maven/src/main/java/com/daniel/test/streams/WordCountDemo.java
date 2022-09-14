package com.daniel.test.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class WordCountDemo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-application");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "175.24.172.160:9092");
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.class.getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.class.getName());

        StreamsBuilder builder = new StreamsBuilder();

        //设置input topic
        KStream<String, String> textLines = builder.stream("streams-plain-text-input");
        //处理
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(":\\W+")))
                .groupBy((key, value) -> value)
                .count();

        //设置output topic
        wordCounts.toStream().to("streams-word-count-output", Produced.with(Serdes.String(), Serdes.Long()));

        //启动
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();
    }
}
