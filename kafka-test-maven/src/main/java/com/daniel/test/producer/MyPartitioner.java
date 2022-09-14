package com.daniel.test.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class MyPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartition = partitions.size();
        if (keyBytes == null) {
            return 0;
        } else {
            return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartition;
        }
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> map) {}
}
