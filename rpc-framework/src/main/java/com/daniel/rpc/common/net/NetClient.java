package com.daniel.rpc.common.net;

import com.daniel.rpc.common.discoverer.ServiceInfo;

/**
 * 网络层处理
 * @author daniel
 */
public interface NetClient {

    /**
     * 发送请求
     * @param data 请求数据
     * @param serviceInfo 服务信息
     * @return 返回数据
     */
    byte[] sendRequest(byte[] data, ServiceInfo serviceInfo);
}
