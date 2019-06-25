package com.arbfintech.microservice.loan;

import com.arbfintech.framework.component.core.constant.GlobalConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {GlobalConst.SCAN_BASE_PACKAGE})
@EnableEurekaClient
@EnableJpaRepositories
@EnableFeignClients
public class LoanApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        SpringApplication.run(LoanApplication.class, args);
    }

}
