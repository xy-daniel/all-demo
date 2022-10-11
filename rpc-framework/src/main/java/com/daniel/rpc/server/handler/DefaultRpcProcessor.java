//package com.daniel.rpc.server.handler;
//
//import com.daniel.rpc.client.ClientStubProxyFactory;
//import com.daniel.rpc.server.RpcServer;
//import com.daniel.rpc.server.register.ServiceObject;
//import com.daniel.rpc.server.register.ServiceRegister;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * RPC处理者，支持服务启动暴露、自动注入Service
// * @author zarlic
// * @date 2021.12.15 20:33
// */
//public class DefaultRpcProcessor implements ApplicationListener<ContextRefreshedEvent> {
//    /**
//     * ApplicationContext事件机制是观察者设计模式的实现，通过ApplicationEvent类和ApplicationListener接口，可以实现ApplicationContext事件处理
//     * 如果容器中有一个ApplicationListener Bean，每当ApplicationContext发布ApplicationEvent时，ApplicationListener Bean将自动被触发。这种事件机制都必须需要程序显示的触发。
//     * @param event
//     */
//
//    @Resource
//    private ClientStubProxyFactory clientStubProxyFactory;
//
//    @Resource
//    private ServiceRegister serviceRegister;
//
//    @Resource
//    private RpcServer rpcServer;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if(Objects.isNull(event.getApplicationContext().getParent())){
//            ApplicationContext context  = event.getApplicationContext();
//
//            // 开启服务
//            startServer(context);
//
//            // 注入Service
//            injectService(context);
//
//        }
//    }
//
//    private void injectService(ApplicationContext context) {
//        String[] names = context.getBeanDefinitionNames();
//        for (String name : names) {
//            Class<?> clazz = context.getType(name);
//            if (Objects.isNull(clazz)) {
//                continue;
//            }
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                InjectService injectLeisure = field.getAnnotation(InjectService.class);
//                if (Objects.isNull(injectLeisure)) continue;
//                Class<?> fieldClass = field.getType();
//                Object object = context.getBean(name);
//                field.setAccessible(true);
//                try {
//                    field.set(object, clientStubProxyFactory.getProxy(fieldClass));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void startServer(ApplicationContext context) {
//        Map<String, Object> beans = context.getBeansWithAnnotation(Service.class);
//        if (beans.size() != 0) {
//            boolean startServerFlag = true;
//            for (Object obj : beans.values()) {
//
//                try {
//                    Class<?> clazz = obj.getClass();
//                    Class<?>[] interfaces = clazz.getInterfaces();
//                    ServiceObject so;
//                    if(interfaces.length != 1){
//                        Service service = clazz.getAnnotation(Service.class);
//                        String value = service.value();
//                        if(value.equals("")){
//                            startServerFlag = false;
//                            throw new UnsupportedOperationException("The exposed interface is not specific with '" + obj.getClass().getName() + "'");
//                        }
//                        so = new ServiceObject(value, (Class<?>) Class.forName(value),obj);
//                    }else {
//                        Class<?> superClass = interfaces[0];
//                        so = new ServiceObject(superClass.getName(), (Class<?>) superClass,obj);
//                    }
//                    serviceRegister.register(so);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (startServerFlag) {
//                rpcServer.start();
//            }
//        }
//
//    }
//}
