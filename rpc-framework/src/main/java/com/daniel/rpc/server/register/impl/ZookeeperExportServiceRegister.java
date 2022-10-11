package com.daniel.rpc.server.register.impl;

import com.alibaba.fastjson.JSON;
import com.daniel.rpc.server.register.ServiceInfo;
import com.daniel.rpc.server.register.ServiceObject;
import com.daniel.rpc.server.register.ServiceRegister;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

/**
 * Zookeeper服务注册器，提供服务注册、服务暴露的能力
 *
 * @author daniel
 */
public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister {

    /**
     * ZK客户端
     */
    private final ZkClient client;

    String zkAddress = "175.24.172.160:2181";

    public ZookeeperExportServiceRegister() {
        client = new ZkClient(zkAddress);
        client.setZkSerializer(new SerializableSerializer());
    }

    /**
     * 服务注册
     */
    @Override
    public void register(ServiceObject so, String protocol, int port) throws Exception {
        super.register(so, protocol, port);
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress() + ":" + port);
        serviceInfo.setName(so.getaInterface().getName());
        serviceInfo.setProtocol(protocol);
        this.exportService(serviceInfo);
    }

    private void exportService(ServiceInfo serviceResource) {
        String serviceName = serviceResource.getName();
        String uri = JSON.toJSONString(serviceResource);
        try {
            uri = URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String servicePath = "/rpc-framework/" + serviceName + "/service";
        if (!client.exists(servicePath)) {
            client.createPersistent(servicePath, true);
        }
        String uriPath = servicePath + "/" + uri;
        if (client.exists(uriPath)) {
            client.delete(uriPath);
        }
        client.createEphemeral(uriPath);
    }
}
