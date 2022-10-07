package com.daniel.zuul.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("daniel.zuul.token-filter")
public class TokenConfiguration {

    private List<String> noAuthRoutes;

    public List<String> getNoAuthRoutes() {
        return noAuthRoutes;
    }

    public void setNoAuthRoutes(List<String> noAuthRoutes) {
        this.noAuthRoutes = noAuthRoutes;
    }
}
