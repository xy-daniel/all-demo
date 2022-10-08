package com.daniel.springcloudgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 自定义过滤器,名称要严格按照这样的写法,否则在配置文件中就要书写全称
 */
public class JwtCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtCheckGatewayFilterFactory.Config> {

    public JwtCheckGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            new RuntimeException().printStackTrace();
            String jwtToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (jwtToken != null) {
                return chain.filter(exchange);
            }
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpHeaders.add(HttpHeaders.CACHE_CONTROL,
                    CacheControl.noStore().getHeaderValue() + "," +
                            CacheControl.noCache().getHeaderValue() + "," +
                            CacheControl.empty().mustRevalidate().getHeaderValue() + "," +
                            CacheControl.maxAge(Duration.ZERO).getHeaderValue()
            );
            String warningStr = "未登录或登录超时";
            DataBuffer buffer = response.bufferFactory().wrap(warningStr.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        };
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}
