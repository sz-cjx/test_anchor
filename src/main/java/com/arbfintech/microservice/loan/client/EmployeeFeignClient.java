package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("maintenance-restapi")
public interface EmployeeFeignClient {
    @GetMapping("/employees/level")
    public String getAgentLevel(@RequestParam(value = "employeeNo") String employeeNo);

    @GetMapping("/departments/category")
    public Integer getCategoryByEmployeeNo(@RequestParam(value = "employeeNo") String employeeNo);

}
