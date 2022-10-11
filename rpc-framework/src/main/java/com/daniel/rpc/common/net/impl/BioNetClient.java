package com.daniel.rpc.common.net.impl;

import com.daniel.rpc.common.discoverer.ServiceInfo;
import com.daniel.rpc.common.net.NetClient;

/**
 * Bio网络客户端
 * @author daniel
 */
public class BioNetClient implements NetClient {
    @Override
    public byte[] sendRequest(byte[] data, ServiceInfo serviceInfo) {
        return new byte[0];
    }
}
