package com.daniel.rpc.demo.provider;

import com.daniel.rpc.common.protocol.impl.JavaSerializeMessageProtocol;
import com.daniel.rpc.demo.service.DemoService;
import com.daniel.rpc.demo.service.impl.DemoServiceImpl;
import com.daniel.rpc.server.RpcServer;
import com.daniel.rpc.server.child.NettyRpcServer;
import com.daniel.rpc.server.handler.RequestHandler;
import com.daniel.rpc.server.register.ServiceObject;
import com.daniel.rpc.server.register.impl.ZookeeperExportServiceRegister;

import java.awt.*;

/**
 * 服务提供者
 * @author daniel
 */
public class Provider {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String protocol = "javas";

        //服务注册
        ZookeeperExportServiceRegister serviceRegister = new ZookeeperExportServiceRegister();
        DemoService demoService = new DemoServiceImpl();
        ServiceObject serviceObject = new ServiceObject(DemoService.class.getName(), DemoService.class, demoService);
        serviceRegister.register(serviceObject, protocol, port);

        RequestHandler requestHandler = new RequestHandler(new JavaSerializeMessageProtocol(), serviceRegister);

        RpcServer rpcServer = new NettyRpcServer(port, protocol, requestHandler);
        rpcServer.start();
        System.in.read();
        rpcServer.stop();

    }
}
