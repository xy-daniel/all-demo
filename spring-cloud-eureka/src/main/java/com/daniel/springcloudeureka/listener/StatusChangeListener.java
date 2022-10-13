package com.daniel.springcloudeureka.listener;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StatusChangeListener {

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        System.out.println(event.getServerId() + " " + event.getAppName() + "已下线");
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        System.out.println(event.getInstanceInfo().getAppName() + "已注册");
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        System.err.println("服务注册中心已启动~");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        System.err.println("eureka服务启动~");
    }
}
