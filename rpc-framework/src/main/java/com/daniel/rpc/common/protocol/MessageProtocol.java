package com.daniel.rpc.common.protocol;

import com.daniel.rpc.common.protocol.transform.Request;
import com.daniel.rpc.common.protocol.transform.Response;

import java.io.*;

/**
 * @author daniel
 */
public interface MessageProtocol {

    /**
     * 请求放编组
     * @param request 请求体
     * @return 编组为字节数组
     * @throws IOException IO异常
     */
    byte[] marshallingRequest(Request request) throws IOException;

    /**
     * 请求放解组
     * @param responseData 字节数组响应数据
     * @return 解组为响应对象
     * @throws IOException IO异常
     * @throws ClassNotFoundException 类未发现异常
     */
    Response unmarshallingResponse(byte[] responseData) throws IOException, ClassNotFoundException;

    /**
     * 响应方编组
     * @param response 响应体
     * @return 编组为字节数组
     * @throws IOException IO异常
     */
    byte[] marshallingResponse(Response response) throws IOException;

    /**
     * 响应方解组
     * @param requestData 请求字节数组
     * @return 解组为响应对象
     * @throws IOException IO异常
     * @throws ClassNotFoundException 类未发现异常
     */
    Request unmarshallingRequest(byte[] requestData) throws IOException, ClassNotFoundException;




}
