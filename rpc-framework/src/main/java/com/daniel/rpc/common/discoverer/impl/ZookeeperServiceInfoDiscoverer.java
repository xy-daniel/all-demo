package com.daniel.rpc.common.discoverer.impl;

import com.alibaba.fastjson.JSON;
import com.daniel.rpc.common.discoverer.ServiceInfo;
import com.daniel.rpc.common.discoverer.ServiceInfoDiscoverer;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * zookeeper远程服务发现
 * @author daniel
 */
public class ZookeeperServiceInfoDiscoverer implements ServiceInfoDiscoverer {

    ZkClient zkClient;

    private String centerRootPath = "/rpc-framework";

    public ZookeeperServiceInfoDiscoverer() {
        zkClient = new ZkClient("175.24.172.160:2181", 50000);
        zkClient.setZkSerializer(new SerializableSerializer());
    }

    @Override
    public List<ServiceInfo> getServiceInfo(String serviceName) {
        String servicePath = centerRootPath + "/" + serviceName + "/service";
        List<String> children = zkClient.getChildren(servicePath);
        ArrayList<ServiceInfo> resources = new ArrayList<>();
        for (String child : children) {
            try {
                String decodeChild = URLDecoder.decode(child, "UTF-8");
                ServiceInfo serviceInfo = JSON.parseObject(decodeChild, ServiceInfo.class);
                resources.add(serviceInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resources;
    }
}
