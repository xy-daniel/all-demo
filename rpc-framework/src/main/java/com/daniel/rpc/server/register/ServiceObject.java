package com.daniel.rpc.server.register;

import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.demo.service.DemoService;
import com.daniel.rpc.server.register.impl.ZookeeperExportServiceRegister;

/**
 * 服务注册对象
 * @author daniel
 */
public class ServiceObject {

    private String name;

    private Class<?> aInterface;

    Object obj;

    public ServiceObject(String name, Class<?> demoServiceClass, Object obj) {
        this.name = name;
        this.aInterface = demoServiceClass;
        this.obj = obj;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getaInterface() {
        return aInterface;
    }

    public void setaInterface(Class<?> aInterface) {
        this.aInterface = aInterface;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Request getClazz() {
        return null;
    }
}
