package com.daniel.rpc.common.discoverer.impl;

import com.daniel.rpc.common.discoverer.ServiceInfo;
import com.daniel.rpc.common.discoverer.ServiceInfoDiscoverer;

import java.util.List;

/**
 * 本地配置文件发现服务
 * @author daniel
 */
public class LocalConfigServiceInfoDiscoverer implements ServiceInfoDiscoverer {
    @Override
    public List<ServiceInfo> getServiceInfo(String serviceName) {
        return null;
    }
}
