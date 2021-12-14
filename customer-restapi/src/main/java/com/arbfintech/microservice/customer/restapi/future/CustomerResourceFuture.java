package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.constant.CustomerFeatureKey;
import com.arbfintech.microservice.customer.object.dto.CustomerProfileDTO;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Jerry
 * @date 2021/12/14 16:05
 */
@Component
public class CustomerResourceFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResourceFuture.class);

    @Autowired
    private CommonReader commonReader;

    public CompletableFuture<String> getProfileByFeature(CustomerProfileDTO customerProfileDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        switch (customerProfileDTO.getProfileFeature()) {
                            case CustomerFeatureKey.PERSONAL: {
                                return AjaxResult.success(commonReader.getEntityByCustomerId(CustomerProfile.class, customerProfileDTO.getCustomerId()));
                            }
                            case CustomerFeatureKey.EMPLOYMENT: {
                                return null;
                            }
                            default:
                                throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_GET_PROFILE_NOT_EXIST);
                        }
                    } catch (ProcedureException e) {
                        LOGGER.warn("[Get Profile]Failure to get profile data. CustomerId: {}, Feature:{}", customerProfileDTO.getCustomerId(), customerProfileDTO.getProfileFeature());
                        return AjaxResult.failure(e);
                    }
                }
        );
    }
}
