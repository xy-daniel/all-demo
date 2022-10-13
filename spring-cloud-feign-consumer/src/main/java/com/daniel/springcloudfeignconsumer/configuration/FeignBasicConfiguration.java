package com.daniel.springcloudfeignconsumer.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignBasicConfiguration {

    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "123456");
    }

}
