package com.sztus.azeroth.microservice.customer.server;

import com.sztus.framework.component.core.constant.GlobalConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = GlobalConst.SCAN_BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = GlobalConst.SCAN_BASE_PACKAGE)
@EnableTransactionManagement
public class CustomerExtApplication {

    /**
     * xxxxxxxxxxxxxxxxxxxxx
     * @param args
     */
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        SpringApplication.run(CustomerExtApplication.class, args);
    }
}
