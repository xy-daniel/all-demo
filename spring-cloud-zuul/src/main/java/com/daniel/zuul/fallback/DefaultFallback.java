package com.daniel.zuul.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class DefaultFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            @NonNull
            public HttpStatus getStatusCode() {
                return HttpStatus.BAD_GATEWAY;
            }

            @Override
            public int getRawStatusCode() {
                return 502;
            }

            @Override
            @NonNull
            public String getStatusText() {
                return "网关出了点小问题";
            }

            @Override
            public void close() {

            }

            @Override
            @NonNull
            public InputStream getBody() {
                return new ByteArrayInputStream("hystrix发现了问题".getBytes(StandardCharsets.UTF_8));
            }

            @Override
            @NonNull
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };
    }
}
