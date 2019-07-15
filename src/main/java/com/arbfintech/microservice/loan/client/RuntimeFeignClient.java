package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wade He
 */

@FeignClient("cache-server")
@RequestMapping("/runtime")
public interface RuntimeFeignClient {

	@GetMapping("/portfolio-parameters/{portfolioId}")
	public String getPortfolioParameter(@PathVariable("portfolioId") Integer portfolioId);
}
