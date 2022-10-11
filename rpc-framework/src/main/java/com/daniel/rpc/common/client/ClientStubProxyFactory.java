package com.daniel.rpc.common.client;

import com.daniel.rpc.common.net.NetClient;
import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.common.protocol.transform.Response;
import com.daniel.rpc.common.discoverer.ServiceInfo;
import com.daniel.rpc.common.protocol.MessageProtocol;
import com.daniel.rpc.common.discoverer.ServiceInfoDiscoverer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author daniel
 */
public class ClientStubProxyFactory {

    private ServiceInfoDiscoverer sid;

    private Map<String, MessageProtocol> supportMessageProtocols;

    private NetClient netClient;

    private Map<Class<?> ,Object> objectCache = new HashMap<>();

    public <T> T getProxy(Class<T> aInterfaces) {
        T obj = (T) this.objectCache.get(aInterfaces);
        if (obj == null) {
            obj = (T) Proxy.newProxyInstance(aInterfaces.getClassLoader(), new Class<?>[] {aInterfaces}, new ClientStubInvocationHandler(aInterfaces));
            this.objectCache.put(aInterfaces, obj);
        }
        return obj;
    }

    public ServiceInfoDiscoverer getSid() {
        return sid;
    }

    public void setSid(ServiceInfoDiscoverer sid) {
        this.sid = sid;
    }

    public Map<String, MessageProtocol> getSupportMessageProtocols() {
        return supportMessageProtocols;
    }

    public void setSupportMessageProtocols(Map<String, MessageProtocol> supportMessageProtocols) {
        this.supportMessageProtocols = supportMessageProtocols;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    private class ClientStubInvocationHandler implements InvocationHandler {

        private final Class<?> aInterface;

        private final Random random = new Random();

        public <T> ClientStubInvocationHandler(Class<T> aInterfaces) {
            super();
            this.aInterface = aInterfaces;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String toStringMethodName = "toString";
            if (toStringMethodName.equals(method.getName())) {
                return proxy.getClass().toString();
            }
            String hashCodeMethodName = "hashCode";
            if (hashCodeMethodName.equals(method.getName())) {
                return 0;
            }

            //1.获得服务信息
            String serviceName = this.aInterface.getName();
            List<ServiceInfo> serviceInfos = sid.getServiceInfo(serviceName);

            if (serviceInfos == null || serviceInfos.size() == 0) {
                throw new Exception("远程服务不存在");
            }

            //随机选择一个服务提供者(软负载均衡)
            ServiceInfo serviceInfo = serviceInfos.get(random.nextInt(serviceInfos.size()));

            //2.构造request对象
            Request request = new Request();
            request.setServiceName(serviceInfo.getName());
            request.setMethod(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);

            //3.协议编组
            //获得该方法对应的协议
            MessageProtocol messageProtocol = supportMessageProtocols.get(serviceInfo.getProtocol());
            //编组请求
            byte[] data = messageProtocol.marshallingRequest(request);

            //4.调用网络层发送请求
            byte[] responseData = netClient.sendRequest(data, serviceInfo);

            //5.解组响应消息
            Response response = messageProtocol.unmarshallingResponse(responseData);

            //6.结果处理
            if (response.getException() != null) {
                throw response.getException();
            }

            return response.getReturnValue();
        }
    }
}
