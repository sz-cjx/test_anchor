package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("microservice-business")
@RequestMapping("/business-grab")
public interface GrabLoanFeignClient {

    @PostMapping("/loan-grab")
    public String addGrabService(@RequestBody String grabInfoStr);

    @GetMapping("/loan-grabbed")
    public String fetchGrabbedLoans(@RequestParam(value = "operatorNo") String operatorNo);

    @GetMapping("/grab-loan/time-out")
    public String handleGrabLoanTimeOut(@RequestParam(value = "grabLoanId") Integer grabLoanId,
                                        @RequestParam(value = "levelFlag")Integer levelFlag);

    @PostMapping("/grab-accept")
    public String acceptGrab(@RequestParam(value = "grabId") Integer grabId);

    @PostMapping("/grab-reject")
    public String rejectGrab(@RequestParam(value = "grabId") Integer grabId);

}
