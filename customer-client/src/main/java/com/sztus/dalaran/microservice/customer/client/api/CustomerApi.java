package com.sztus.dalaran.microservice.customer.client.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "microservice-customer", path = "/v4", contextId = "customer-api")
public interface CustomerApi {
}
