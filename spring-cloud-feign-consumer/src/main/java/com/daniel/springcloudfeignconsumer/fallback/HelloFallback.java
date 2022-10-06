package com.daniel.springcloudfeignconsumer.fallback;

import com.daniel.springcloudfeignconsumer.service.HelloService;
import org.springframework.stereotype.Component;

@Component
public class HelloFallback implements HelloService {

    @Override
    public String index() {
        return "出错了,降级";
    }

    @Override
    public String getName(String name) {
        System.out.println("请求数据:name=" + name);
        return "getName出错了";
    }

    @Override
    public String getName(String id, String name) {
        System.out.println("请求数据:id=" + id + ",name=" + name);
        return "testName出错了";
    }

    @Override
    public String test(String body) {
        return "123";
    }
}
