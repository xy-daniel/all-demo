package com.daniel.rpc.server.handler;

import com.daniel.rpc.common.protocol.MessageProtocol;
import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.common.protocol.transform.Response;
import com.daniel.rpc.common.protocol.transform.Status;
import com.daniel.rpc.server.register.ServiceObject;
import com.daniel.rpc.server.register.ServiceRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求处理者，提供解组请求、编组响应等操作
 *
 * @author zarlic
 * @date 2021.12.15 20:35
 */
public class RequestHandler {
    private MessageProtocol protocol;

    private ServiceRegister serviceRegister;

    public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
        super();
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public byte[] handleRequest(byte[] data) throws Exception {
        // 1、解组消息
        Request request = this.protocol.unmarshallingRequest(data);

        // 2、查找服务对象
        ServiceObject so = this.serviceRegister.getServiceObject(request.getServiceName());

        Response response = null;

        if (so == null) {
            response = new Response(Status.NOT_FOUND);
        } else {
            // 3、反射调用对应的过程方法
            try {
                Method method = so.getaInterface().getMethod(request.getMethod(), request.getParameterTypes());
                Object returnValue = method.invoke(so.getObj(), request.getParameters());
                response = new Response(Status.SUCCESS);
                response.setReturnValue(returnValue);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                response = new Response(Status.ERROR);
                response.setException(e);
            }
        }
        // 4、编组响应消息
        return this.protocol.marshallingResponse(response);
    }
}
