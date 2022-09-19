package com.daniel.redis;

import redis.clients.jedis.Jedis;

public class RedisDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("175.24.172.160", 6379);
        jedis.auth("123456");
        jedis.set("name", "daniel");
        String name = jedis.get("name");
        System.out.println(name);
    }
}
