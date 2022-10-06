package com.daniel.definedfegin.utils;

import com.daniel.definedfegin.annotation.FeignClient;
import com.daniel.definedfegin.annotation.FeignGet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.Set;

public class FeignRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanClassLoaderAware, ResourceLoaderAware, BeanFactoryAware {

    //扫描 - 扫描到我们的自动以注解的bean,获取到bean上面的注解对象,并获取到相应的属性
    //为每一个bean生成一个动态代理,通过动态代理去发起请求
    //希望我们能够把响应的bean放入到spring容器中

    private Environment environment;

    private ClassLoader classLoader;

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //只有扫描到我们的自定义注解之后,我们才能去操作
        //扫描到了之后需要有一个classLoader这样才能去加载到我们的容器中
        try {
            registerHttpRequest(beanDefinitionRegistry);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册动态bean
     */
    private void registerHttpRequest(BeanDefinitionRegistry beanDefinitionRegistry) throws ClassNotFoundException {
        //扫描我们的类,然后加载
        ClassPathScanningCandidateComponentProvider classScanner = getClassScanner();
        classScanner.setResourceLoader(this.resourceLoader);
        //指定标注了自定义注解的接口
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignClient.class);
        classScanner.addIncludeFilter(annotationTypeFilter);
        //扫描
        String basePackage = "com.daniel";
        Set<BeanDefinition> beanDefinitionSet = classScanner.findCandidateComponents(basePackage);

        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                String className = beanDefinition.getBeanClassName();
                //创建动态代理,并将我们的带注解的接口注册
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                assert className != null;
                ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(className, createProxy(annotatedBeanDefinition));
            }
        }
    }

    //创建动态代理
    private Object createProxy(AnnotatedBeanDefinition annotatedBeanDefinition) throws ClassNotFoundException {
        AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
        Class<?> target = Class.forName(annotationMetadata.getClassName());
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //拿到我们的注解
                FeignClient feignClient = target.getAnnotation(FeignClient.class);
                String baseUrl = feignClient.baseUrl();
                if (method.getAnnotation(FeignGet.class) != null) {
                    FeignGet feignGet = method.getAnnotation(FeignGet.class);
                    String requestUrl = baseUrl + feignGet.url();
                    //发起请求
                    System.out.println("有了请求地址,可以发起请求了");
                    return new RestTemplate().getForObject(requestUrl, String.class, "");
                }
                throw new IllegalArgumentException("不符合要求");
            }
        };
        return Proxy.newProxyInstance(this.classLoader, new Class[]{target}, invocationHandler);
    }

    private ClassPathScanningCandidateComponentProvider getClassScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, FeignRegister.this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                try {
                    if (beanDefinition.getMetadata().isInterface()) {
                        Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(), classLoader);
                        return !target.isAnnotation();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return super.isCandidateComponent(beanDefinition);
            }
        };
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
