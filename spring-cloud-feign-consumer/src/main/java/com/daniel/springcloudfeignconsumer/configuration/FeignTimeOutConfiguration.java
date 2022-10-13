package com.daniel.springcloudfeignconsumer.configuration;

import feign.Request;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTimeOutConfiguration {

    public Request.Options options() {
        //第一个参数是连接超时,后一个是获得结果超时
        return new Request.Options(5000, 10000);
    }

}
