package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@FeignClient(name = "microservice-timeline")
@RequestMapping("/timeline")
public interface TimelineFeignClient {
	

	@GetMapping("/loan-status")
	public String getLoanStatusByContractNo(@RequestParam("contractNo") String contractNo);

//	@PostMapping("/timelines")
//	public String addTimeline(@RequestBody String timelineInfo);

	@PostMapping("/loan-timelines")
	public String addTimeline(@RequestBody String dataStr);

	@GetMapping("/todolist-contractNo")
	public HashSet getToDoListContractNo(@RequestParam(value = "operatorNo") String operatorNo,
										 @RequestParam(value = "eventtype") Integer eventtype);

}
