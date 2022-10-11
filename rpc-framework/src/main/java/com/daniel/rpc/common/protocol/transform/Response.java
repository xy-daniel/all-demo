package com.daniel.rpc.common.protocol.transform;

import java.io.Serializable;
import java.util.Map;

/**
 * 服务端响应
 * @author daniel
 */
public class Response implements Serializable {

    static final long serialVersionUID = 2L;

    private Status status;

    private Map<String, String> headers;

    private Class<?> returnType;

    private Object returnValue;

    private Exception exception;

    public Response(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
