package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.microservice.customer.object.dto.CustomerDocumentDTO;
import com.arbfintech.microservice.customer.object.enumerate.CustomerDocumentTypeEnum;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.DocumentCategoryTypeEnum;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.reader.CustomerDocumentReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerDocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDocumentService.class);

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    @Autowired
    private CustomerDocumentReader customerDocumentReader;

    private static final List<Integer> DOCUMENT_ACCOUNT_TYPE_LIST = Arrays.asList(
            CustomerDocumentTypeEnum.SSN.getValue(),
            CustomerDocumentTypeEnum.DRIVER_LICENSE.getValue(),
            CustomerDocumentTypeEnum.GRADUATION_CERTIFICATE.getValue()
    );

    public String listDocument(CustomerDocumentDTO customerDocumentDTO) throws ProcedureException {
        DocumentCategoryTypeEnum documentCategoryTypeEnum = Optional.ofNullable(EnumUtil.getByValue(DocumentCategoryTypeEnum.class, customerDocumentDTO.getDocumentCategory()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.FAILURE_DOCUMENT_CATEGORY_NOT_EXIST));
        Long customerId = customerDocumentDTO.getCustomerId();

        switch (documentCategoryTypeEnum) {
            case LOAN:
                return AjaxResult.success();
            case ACCOUNT:
                return AjaxResult.success(customerDocumentReader.listDocument(customerId, DOCUMENT_ACCOUNT_TYPE_LIST));
                default:
                    throw new ProcedureException(CustomerErrorCode.FAILURE_DOCUMENT_CATEGORY_NOT_EXIST);
        }
    }
}
