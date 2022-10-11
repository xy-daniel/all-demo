package com.daniel.rpc.server.register;

/**
 * 暴露出去的服务信息对象
 * @author daniel
 */
public class ServiceInfo {

    private String address;

    private String name;

    private String protocol;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
