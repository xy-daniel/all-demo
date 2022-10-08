package com.daniel.simple.listener;

import com.daniel.simple.event.MyRemoteEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyRemoteListener implements ApplicationListener<MyRemoteEvent> {

    @Override
    public void onApplicationEvent(MyRemoteEvent event) {
        System.out.println("myRemoteListener收到远程消息 - " + event.getSource());
    }
}
