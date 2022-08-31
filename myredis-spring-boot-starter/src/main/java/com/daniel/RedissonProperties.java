package com.daniel;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author daniel
 */
@ConfigurationProperties(prefix = "com.daniel")
public class RedissonProperties {
    private String host = "127.0.0.1";
    private int port = 6379;
    private String password = "";
    private int timeout;
    private boolean ssl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
