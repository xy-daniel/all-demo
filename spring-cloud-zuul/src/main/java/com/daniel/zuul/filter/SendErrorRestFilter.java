package com.daniel.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SendErrorRestFilter extends SendErrorFilter {

    @Override
    public String filterType() {
        return super.filterType();
    }

    @Override
    public int filterOrder() {
        return super.filterOrder();
    }

    @Override
    public boolean shouldFilter() {
        return super.shouldFilter();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        try {
            int responseStatusCode = ctx.getResponseStatusCode();
            // 此处自定义响应体start
            String cumstomBody = "{\"code\":\"" + responseStatusCode + "\"}";//内容省略...
            // 此处自定义响应体end
            response.setStatus(ctx.getResponseStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(cumstomBody.getBytes());
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }
}
