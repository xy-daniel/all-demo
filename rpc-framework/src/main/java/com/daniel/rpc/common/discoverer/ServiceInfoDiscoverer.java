package com.daniel.rpc.common.discoverer;

import java.util.List;

/**
 * @author daniel
 */
public interface ServiceInfoDiscoverer {

    /**
     * 根据服务名称获取服务接口
     * @param serviceName 服务名称
     * @return 服务列表
     */
    List<ServiceInfo> getServiceInfo(String serviceName);
}
