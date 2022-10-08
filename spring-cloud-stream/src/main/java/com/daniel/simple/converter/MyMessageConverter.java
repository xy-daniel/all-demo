package com.daniel.simple.converter;

import com.sun.org.apache.xpath.internal.operations.String;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

public class MyMessageConverter extends AbstractMessageConverter {

    public MyMessageConverter() {
        super(new MimeType("application", "user"));
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        //判断当前对象是否转换:return User.class.equals(aClass);
        return true;
    }

    /**
     * 消息转换为对象
     * @param message
     * @param targetClass
     * @param conversionHint
     * @return
     */
    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        System.out.println("执行消息到对象的转换");
        Object payload = message.getPayload();
        //根据需求自行转换即可,比如json --> xml
        return payload.toString();
    }

    /**
     * 对象转换为消息
     * @param payload
     * @param headers
     * @param conversionHint
     * @return
     */
    @Override
    protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        System.out.println("执行对象到消息的转换");
        //xml --> json
        return payload;
    }
}
