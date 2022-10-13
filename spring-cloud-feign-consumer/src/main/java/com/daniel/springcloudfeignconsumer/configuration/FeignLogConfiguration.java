package com.daniel.springcloudfeignconsumer.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLogConfiguration {

    /**
     * none: 不输出日志
     * basic: 只输出请求方法的url和响应状态码以及接口执行时间
     * head: 输出完整的头信息
     * full: 全部日志
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
