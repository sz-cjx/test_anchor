package com.arbfintech.microservice.customer.restapi;

import com.arbfintech.framework.component.core.constant.GlobalConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = GlobalConst.SCAN_BASE_PACKAGE)
@EnableFeignClients(basePackages = GlobalConst.SCAN_BASE_PACKAGE)
@EnableEurekaClient
@EnableJpaAuditing
public class CustomerRestApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerRestApiApplication.class,args);
    }
}
