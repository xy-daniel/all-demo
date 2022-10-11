package com.daniel.rpc.common.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.common.protocol.transform.Response;
import com.daniel.rpc.common.protocol.MessageProtocol;

import java.io.IOException;

/**
 * json协议编解组
 * @author daniel
 */
public class JsonMessageProtocol implements MessageProtocol {
    @Override
    public byte[] marshallingRequest(Request request) throws IOException {
        Request temp = new Request();
        temp.setServiceName(request.getServiceName());
        temp.setMethod(request.getMethod());
        temp.setHeaders(request.getHeaders());
        temp.setParameterTypes(request.getParameterTypes());
        Object[] parameters = request.getParameters();
        if (parameters != null) {
            Object[] serializeParams = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                //TODO 这儿存在反序列化问题,被请求方不知道怎么解码
                serializeParams[i] = JSON.toJSONString(parameters[i]);
            }
            temp.setParameters(serializeParams);
        }
        return JSON.toJSONBytes(temp);
    }

    @Override
    public Response unmarshallingResponse(byte[] responseData) throws IOException, ClassNotFoundException {
        //TODO
        return null;
    }

    @Override
    public byte[] marshallingResponse(Response response) throws IOException {
        //TODO
        return null;
    }

    @Override
    public Request unmarshallingRequest(byte[] requestData) throws IOException, ClassNotFoundException {
        Request request = JSON.parseObject(requestData, Request.class);
        if (request.getParameters() != null) {
            //TODO
        }
        return null;
    }
}
