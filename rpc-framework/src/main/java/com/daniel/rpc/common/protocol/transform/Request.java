package com.daniel.rpc.common.protocol.transform;

import java.io.Serializable;
import java.util.Map;

/**
 * @author daniel
 */
public class Request implements Serializable {

    static final long serialVersionUID = 1L;

    private String serviceName;

    private String method;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    private Map<String, String> headers;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
