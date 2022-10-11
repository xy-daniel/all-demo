package com.daniel.rpc.common.discoverer;

/**
 * @author daniel
 */
public class ServiceInfo {

    private String name;

    private String protocol;

    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
