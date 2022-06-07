package com.sztus.azeroth.microservice.customer.client.api;

import com.sztus.azeroth.microservice.customer.client.object.parameter.request.SaveIbvAuthorizationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.ListIbvAuthorizationResponse;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.SaveIbvAuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "microservice-customer-ext", path = "/v4", contextId = "customer-ibv-api")
public interface CustomerIbvApi {

    @PostMapping("/ibv-authorization/save")
    SaveIbvAuthorizationResponse saveIbvAuthorization(
            @RequestBody SaveIbvAuthorizationRequest request
    );

    @GetMapping("/ibv-authorization/list")
    ListIbvAuthorizationResponse listIbvAuthorization(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "portfolioId", required = false) Long portfolioId
    );

}
