package com.arbfintech.microservice.customer.restapi.future;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.constant.CustomerFeatureKey;
import com.arbfintech.microservice.customer.object.dto.CustomerProfileDTO;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.restapi.service.CustomerResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jerry
 * @date 2021/12/14 16:05
 */
@Component
public class CustomerResourceFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResourceFuture.class);

    @Autowired
    private CustomerResourceService customerResourceService;

    public CompletableFuture<String> getProfileByFeature(CustomerProfileDTO customerProfileDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Long customerId = customerProfileDTO.getCustomerId();
                    try {
                        switch (customerProfileDTO.getProfileFeature()) {
                            case CustomerFeatureKey.PERSONAL: {
                                return AjaxResult.success(customerResourceService.getCustomerProfile(customerId));
                            }
                            case CustomerFeatureKey.EMPLOYMENT: {
                                return AjaxResult.success(customerResourceService.getCustomerEmploymentData(customerId));
                            }
                            default:
                                throw new ProcedureException(CustomerErrorCode.FAILURE_PROFILE_NOT_EXIST);
                        }
                    } catch (ProcedureException e) {
                        LOGGER.warn("[Get Profile]Failure to get profile data. CustomerId: {}, Feature:{}", customerId, customerProfileDTO.getProfileFeature());
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> saveProfileByFeature(CustomerProfileDTO customerProfileDTO, HttpServletRequest request) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        // TODO check token
                        JSONObject accountJson = new JSONObject();

                        switch (customerProfileDTO.getProfileFeature()) {
                            case CustomerFeatureKey.PERSONAL: {
                                return null;
                            }
                            case CustomerFeatureKey.EMPLOYMENT: {
                                return null;
                            }
                            default:
                                throw new ProcedureException(CustomerErrorCode.FAILURE_PROFILE_NOT_EXIST);
                        }
                    } catch (ProcedureException e) {
                        LOGGER.warn("[Save Profile]Failure to save profile data. CustomerId: {}, Feature:{}", customerProfileDTO.getCustomerId(), customerProfileDTO.getProfileFeature());
                        return AjaxResult.failure(e);
                    }
                }
        );
    }
}
