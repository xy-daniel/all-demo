package com.daniel.springcloudsleuthserver.service;

import brave.Span;
import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    Tracer tracer;

    /**
     * 这里代表一次数据库操作,通过span把他记录下来
     */
    @NewSpan("userService-database-insert") //注解方式记录下我们的操作
    public int insert() {
        System.out.println("插入数据成功");
        return 1;
    }

    /**
     * 演示创建span
     */
    public void test() {
        Span newSpan = this.tracer.nextSpan()
                .name("userService-test");
        try (Tracer.SpanInScope ignored = this.tracer.withSpanInScope(newSpan)) {
            newSpan.tag("tag-test", "我是一个自定义标签");
            System.out.println("test方法调用成功");
        } finally {
            //span可以跨多个方法,只要没有finish,就可以通过tracer.currentSpan()获取到当前线程内的一个span
            newSpan.finish();
        }
    }
}
