package com.daniel.java;

import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCacheDemo {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<String, User> userCache = CacheBuilder.newBuilder()
                .concurrencyLevel(8)
                .expireAfterWrite(8, TimeUnit.SECONDS)
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(100)
                .recordStats()
                .removalListener(removalNotification -> System.out.println(removalNotification.getKey() + "被移除了,原因:" + removalNotification.getCause()))
                .build(
                        new CacheLoader<String, User>() {
                            @Override
                            public User load(String key) throws Exception {
                                System.out.println("缓存没有时,从数据库加载" + key);
                                return new User("tony" + key, key);
                            }
                        }
                );
        for (int i = 0; i < 20; i++) {
            User user = userCache.get("uid" + i);
            System.out.println(user);
        }
        for (int i = 0; i < 20; i++) {
            User user = userCache.get("uid" + i);
            System.out.println(user);
        }
        System.out.println("cache status:" + userCache.stats().toString());
    }
}
