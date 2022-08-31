package com.daniel;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author daniel
 */
public class RedisClient {
    private String host;
    private Integer port;
    private String password;

    public RedisClient(String host, Integer port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public String redisCli() {
        Config config = new Config();
        config
                .useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password)
                .setConnectTimeout(10000);
        RedissonClient redissonClient = Redisson.create(config);
        RList<Object> nameList = redissonClient.getList("nameList");
        nameList.add("daniel");
        nameList.forEach(System.out::println);
        redissonClient.shutdown();
        return "";
    }
}
