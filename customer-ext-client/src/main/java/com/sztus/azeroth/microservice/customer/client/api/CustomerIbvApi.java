package com.sztus.azeroth.microservice.customer.client.api;

import com.sztus.azeroth.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.ListIbvAuthorizationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.SaveIbvAuthorizationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.ListIbvAuthorizationResponse;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.SaveIbvAuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "microservice-customer-ext", path = "/v4", contextId = "customer-ibv-api")
public interface CustomerIbvApi {

    @PostMapping(CustomerAction.SAVE_IBV_AUTHORIZATION)
    SaveIbvAuthorizationResponse saveIbvAuthorization(
            @RequestBody SaveIbvAuthorizationRequest request
    );

    @GetMapping(CustomerAction.LIST_IBV_AUTHORIZATION)
    ListIbvAuthorizationResponse listIbvAuthorization(
            @SpringQueryMap ListIbvAuthorizationRequest request
    );


}
