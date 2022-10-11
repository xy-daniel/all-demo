package com.daniel.rpc.server.register;

/**
 * 服务注册器，定义服务注册规范
 */
public interface ServiceRegister {

    void register(ServiceObject so, String protocol, int port) throws Exception;

    ServiceObject getServiceObject(String name) throws Exception;
}

