package com.daniel.springcloudfeignconsumer.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignBasicAuthRequest implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //TODO 随便写
    }
}
