package com.daniel.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.daniel.zuul.configuration.TokenConfiguration;
import com.daniel.zuul.jwt.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenValidateFilter extends ZuulFilter {

    @Autowired
    TokenConfiguration tokenConfiguration;
    
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        //过滤url,如果是登录,不做任何校验直接放行
        RequestContext ctx = RequestContext.getCurrentContext();
        return !tokenConfiguration.getNoAuthRoutes().contains(ctx.get("proxy"));
    }

    @Override
    public Object run() throws ZuulException {
        //校验
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("Authorization");
        if (token == null) {
            forbidden();
            return null;
        }
        //解码
        Claims claims = jwtUtils.parseToken(token);
        if (claims == null) {
            forbidden();
            return null;
        }
        System.out.println("请求token内容是:" + JSONObject.toJSONString(claims));
        return null;
    }
    
    void forbidden() {
            RequestContext.getCurrentContext().setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
            ReflectionUtils.rethrowRuntimeException(new ZuulException("无访问权限", HttpStatus.SC_FORBIDDEN, "token校验不通过"));
    }
}
