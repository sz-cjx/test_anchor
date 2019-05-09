package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@FeignClient(name = "microservice-business")
@RequestMapping("/business/timeline")
public interface TimelineFeignClient {

//	@PostMapping("/timelines")
//	public String addTimeline(@RequestBody String timelineInfo);

	@PostMapping("/loan-timelines")
	public String addTimeline(@RequestBody String dataStr);

	@GetMapping("/todo-list")
	public HashSet getToDoListContractNo(@RequestParam(value = "operatorNo") String operatorNo,
										 @RequestParam(value = "operationNameList") String operationNameListStr);

}
