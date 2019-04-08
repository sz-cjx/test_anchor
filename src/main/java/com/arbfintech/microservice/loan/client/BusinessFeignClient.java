package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wade He
 */

@FeignClient("microservice-business")
@RequestMapping("/business")
public interface BusinessFeignClient {
	@GetMapping("/seeds/{maxlength}/{arg1}/{arg2}")
	public String getSeed(
			@PathVariable(value = "maxlength", required = true) Integer maxlength,
			@PathVariable(value = "arg1", required = true) String arg1,
			@PathVariable(value = "arg2", required = false) String arg2);
	
}
