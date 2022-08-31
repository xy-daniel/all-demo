package com.daniel;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author daniel
 */
@EnableConfigurationProperties(RedissonProperties.class)
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisClient redisClient(RedissonProperties redissonProperties) {
        return new RedisClient(redissonProperties.getHost(), redissonProperties.getPort(), redissonProperties.getPassword());
    }

}
