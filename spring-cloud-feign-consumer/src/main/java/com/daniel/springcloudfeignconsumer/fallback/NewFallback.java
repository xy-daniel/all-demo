package com.daniel.springcloudfeignconsumer.fallback;

import com.daniel.springcloudfeignconsumer.service.NewService;
import org.springframework.stereotype.Component;

@Component
public class NewFallback implements NewService {

    @Override
    public String index() {
        return "新服务请求出错了";
    }
}
