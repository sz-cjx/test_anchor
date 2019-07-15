package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("maintenance-restapi")
public interface MaintenanceFeignClient {

    @GetMapping("/holidays/{begin}/{end}")
    String listHolidayByRange(
            @PathVariable("begin") Long begin,
            @PathVariable("end") Long end
    );

    @GetMapping("/email-templates")
    public String listEmailTemplateByPortfolioId(@RequestParam("portfolioId") Integer portfolioId);
}
