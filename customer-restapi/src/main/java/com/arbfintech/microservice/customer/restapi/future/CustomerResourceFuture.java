package com.arbfintech.microservice.customer.restapi.future;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.constant.CustomerFeatureKey;
import com.arbfintech.microservice.customer.object.dto.CustomerOptInDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerProfileDTO;
import com.arbfintech.microservice.customer.object.dto.IbvDTO;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.restapi.service.CustomerResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
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
                    String profileFeature = customerProfileDTO.getProfileFeature();
                    try {
                        switch (profileFeature) {
                            case CustomerFeatureKey.PERSONAL: {
                                return AjaxResult.success(customerResourceService.getCustomerProfile(customerId));
                            }
                            case CustomerFeatureKey.CONTACT: {
                                return AjaxResult.success(customerResourceService.getCustomerContact(customerId));
                            }
                            case CustomerFeatureKey.EMPLOYMENT: {
                                return AjaxResult.success(customerResourceService.getCustomerEmploymentData(customerId));
                            }
                            case CustomerFeatureKey.BANK: {
                                return AjaxResult.success(customerResourceService.listCustomerBankData(customerId));
                            }
                            case CustomerFeatureKey.CARD: {
                                return AjaxResult.success(customerResourceService.listCustomerBankCardData(customerId));
                            }
                            default:
                                throw new ProcedureException(CustomerErrorCode.FAILURE_PROFILE_NOT_EXIST);
                        }
                    } catch (ProcedureException e) {
                        LOGGER.warn("[Get Profile]Failure to get profile data. CustomerId: {}, Feature:{}", customerId, profileFeature);
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> saveProfileByFeature(CustomerProfileDTO customerProfileDTO, HttpServletRequest request) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Long customerId = customerProfileDTO.getCustomerId();
                    String profileFeature = customerProfileDTO.getProfileFeature();
                    JSONObject dataJson = customerProfileDTO.getData();
                    try {
                        // TODO check token
                        JSONObject accountJson = new JSONObject();

                        switch (profileFeature) {
                            case CustomerFeatureKey.PERSONAL: {
                                return customerResourceService.updateCustomerProfile(customerId, dataJson);
                            }
                            case CustomerFeatureKey.CONTACT: {
                                return customerResourceService.updateCustomerContact(customerId, dataJson);
                            }
                            case CustomerFeatureKey.EMPLOYMENT: {
                                return customerResourceService.updateCustomerEmploymentData(customerId, dataJson);
                            }
                            case CustomerFeatureKey.BANK: {
                                return customerResourceService.updateCustomerBankData(customerId, dataJson);
                            }
                            case CustomerFeatureKey.CARD: {
                                return customerResourceService.updateCustomerBankCardData(customerId, dataJson);
                            }
                            default:
                                throw new ProcedureException(CustomerErrorCode.FAILURE_PROFILE_NOT_EXIST);
                        }
                    } catch (ProcedureException e) {
                        LOGGER.warn("[Save Profile]Failure to save profile data. CustomerId: {}, Feature:{}, Exception:", customerId, profileFeature, e);
                        return AjaxResult.failure(e);
                    } catch (ParseException e) {
                        LOGGER.warn("[Save Profile]Failure to parse profile data. CustomerId: {}, Feature:{}", customerId, profileFeature);
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> getOperationLog(String dataStr) {
        return CompletableFuture.supplyAsync(
                () -> customerResourceService.getOperationLog(dataStr)
        );
    }

    public CompletableFuture<String> getCustomerOptIn(Long customerId) {
        return CompletableFuture.supplyAsync(
                () -> customerResourceService.getCustomerOptIn(customerId)
        );
    }

    public CompletableFuture<String> saveCustomerOptIn(CustomerOptInDTO customerOptInDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerResourceService.saveCustomerOptIn(customerOptInDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> getDecisionLogic(Long customerId) {
        return CompletableFuture.supplyAsync(
                () -> customerResourceService.getDecisionLogic(customerId)
        );
    }

    public CompletableFuture<String> authorizationDecisionLogic(IbvDTO ibvDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerResourceService.authorizationDecisionLogic(ibvDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }
}
