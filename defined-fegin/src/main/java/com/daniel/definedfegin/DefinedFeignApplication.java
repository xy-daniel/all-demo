package com.daniel.definedfegin;

import com.daniel.definedfegin.service.TestFeign;
import com.daniel.definedfegin.utils.FeignRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FeignRegister.class)
public class DefinedFeignApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DefinedFeignApplication.class, args);
        TestFeign testFeign = context.getBean(TestFeign.class);
    }

}
