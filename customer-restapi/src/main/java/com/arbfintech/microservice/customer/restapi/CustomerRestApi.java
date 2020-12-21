package com.arbfintech.microservice.customer.restapi;

import com.arbfintech.framework.component.core.constant.GlobalConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.TimeZone;

/**
 * @author Fly_Roushan
 * @date 2020/12/21
 */

@SpringBootApplication(scanBasePackages = GlobalConst.SCAN_BASE_PACKAGE)
@EnableEurekaClient
public class CustomerRestApi {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        SpringApplication.run(CustomerRestApi.class, args);
    }
}
