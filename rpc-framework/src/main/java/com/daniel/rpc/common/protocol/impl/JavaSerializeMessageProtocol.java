package com.daniel.rpc.common.protocol.impl;

import com.daniel.rpc.common.protocol.MessageProtocol;
import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.common.protocol.transform.Response;

import java.io.*;

/**
 * 使用java自带的序列化和反序列化工具类进行编组和解组
 * @author daniel
 */
public class JavaSerializeMessageProtocol implements MessageProtocol {

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] marshallingRequest(Request request) throws IOException {
        return this.serialize(request);
    }

    @Override
    public Response unmarshallingResponse(byte[] responseData) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(responseData));
        return (Response) objectInputStream.readObject();
    }

    @Override
    public byte[] marshallingResponse(Response response) throws IOException {
        return this.serialize(response);
    }

    @Override
    public Request unmarshallingRequest(byte[] requestData) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(requestData));
        return (Request) objectInputStream.readObject();
    }
}
