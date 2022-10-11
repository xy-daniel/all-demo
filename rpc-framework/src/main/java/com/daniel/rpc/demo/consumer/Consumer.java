package com.daniel.rpc.demo.consumer;

import com.daniel.rpc.common.client.ClientStubProxyFactory;
import com.daniel.rpc.common.discoverer.impl.ZookeeperServiceInfoDiscoverer;
import com.daniel.rpc.common.net.impl.NettyNetClient;
import com.daniel.rpc.common.protocol.MessageProtocol;
import com.daniel.rpc.common.protocol.impl.JavaSerializeMessageProtocol;
import com.daniel.rpc.demo.service.DemoService;

import java.awt.*;
import java.util.HashMap;

/**
 * 服务消费者相当于请求人
 * @author daniel
 */
public class Consumer {

    public static void main(String[] args) {
        ClientStubProxyFactory factory = new ClientStubProxyFactory();
        //设置服务发现者
        factory.setSid(new ZookeeperServiceInfoDiscoverer());
        //设置支持的协议
        HashMap<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        supportMessageProtocols.put("javas", new JavaSerializeMessageProtocol());
        factory.setSupportMessageProtocols(supportMessageProtocols);
        //设置网络层实现
        factory.setNetClient(new NettyNetClient());

        DemoService demoService = factory.getProxy(DemoService.class);
        String result = demoService.sayHello("world");
        System.out.println(result);//显示调用结果

        System.out.println(demoService.multiPoint(new Point(5, 10), 2));
    }

}
