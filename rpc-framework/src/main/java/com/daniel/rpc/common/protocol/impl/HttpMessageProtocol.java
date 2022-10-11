package com.daniel.rpc.common.protocol.impl;

import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.common.protocol.transform.Response;
import com.daniel.rpc.common.protocol.MessageProtocol;

import java.io.*;

/**
 * Http协议编解组
 *
 * @author daniel
 */
public class HttpMessageProtocol implements MessageProtocol {
    @Override
    public byte[] marshallingRequest(Request request) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(request);
        bytes = bo.toByteArray();
        bo.close();
        oo.close();
        return (bytes);
    }

    @Override
    public Response unmarshallingResponse(byte[] responseData) throws IOException, ClassNotFoundException {
        Response response;
        //bytearray to object
        ByteArrayInputStream bi = new ByteArrayInputStream(responseData);
        ObjectInputStream oi = new ObjectInputStream(bi);

        response = (Response) oi.readObject();

        bi.close();
        oi.close();
        return response;
    }

    @Override
    public byte[] marshallingResponse(Response response) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(response);
        bytes = bo.toByteArray();
        bo.close();
        oo.close();
        return (bytes);
    }

    @Override
    public Request unmarshallingRequest(byte[] requestData) throws IOException, ClassNotFoundException {
        Request request;
        //bytearray to object
        ByteArrayInputStream bi = new ByteArrayInputStream(requestData);
        ObjectInputStream oi = new ObjectInputStream(bi);

        request = (Request) oi.readObject();

        bi.close();
        oi.close();
        return request;
    }
}
