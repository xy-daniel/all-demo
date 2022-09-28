package com.daniel.activespringconsumer;

import com.daniel.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daniel
 */
@RestController
public class MyRedisTestController {

    @Autowired
    RedisClient redisClient;

    @GetMapping("index")
    public String index() {
        return redisClient.redisCli();
    }

}
