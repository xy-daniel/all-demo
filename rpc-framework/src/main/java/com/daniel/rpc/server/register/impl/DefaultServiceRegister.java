package com.daniel.rpc.server.register.impl;

import com.daniel.rpc.server.register.ServiceObject;
import com.daniel.rpc.server.register.ServiceRegister;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的服务注册实现
 * @author daniel
 */
public class DefaultServiceRegister implements ServiceRegister {

    private final Map<String, ServiceObject> serviceMap = new HashMap<>();

    @Override
    public void register(ServiceObject so, String protocol, int port) throws Exception {
        if (so == null) {
            throw new IllegalArgumentException("Parameter cannot be empty.");
        }
        this.serviceMap.put(so.getName(), so);
    }

    @Override
    public ServiceObject getServiceObject(String name) {
        return serviceMap.get(name);
    }
}
