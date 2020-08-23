package com.arbfintech.microservice.customer.clientapi.future;

import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.framework.component.database.core.SimpleProcedure;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Baron
 */
@Component
public class CustomerFuture {

    @Autowired
    private SimpleProcedure simpleProcedure;

    public CompletableFuture<String> getCustomerByConditions(Object... conditions) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SqlOption sqlOption = generateSqlOptionByConditions(conditions);

                Customer customer = simpleProcedure.findByOptions(Customer.class, sqlOption.toString());
                if (Objects.isNull(customer)) {
                    throw new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
                }

                return AjaxResult.success(customer);
            } catch (ProcedureException e) {
                return AjaxResult.failure(e);
            } catch (Exception e) {
                return AjaxResult.failure();
            }
        });
    }

    public CompletableFuture<String> listCustomerByConditions(Object... conditions) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SqlOption sqlOption = generateSqlOptionByConditions(conditions);

                List<Customer> customerList = simpleProcedure.findAllByOptions(Customer.class, sqlOption.toString());
                return AjaxResult.success(customerList);
            } catch (ProcedureException e) {
                return AjaxResult.failure(e);
            } catch (Exception e) {
                return AjaxResult.failure();
            }
        });
    }

    private SqlOption generateSqlOptionByConditions(Object... conditions) throws ProcedureException {
        final String[] KEY_SET = {"ssn", "email"};

        SqlOption sqlOption = SqlOption.getInstance();

        int nullCount = 0;
        for (int i = 0; i < conditions.length; ++i) {
            String key = KEY_SET[i];
            Object value = conditions[i];

            if (value != null) {
                sqlOption.addWhere(
                        String.format("%s = :%s", StringUtil.formatCamelToUnderscore(key), key),
                        ConditionTypeConst.AND,
                        key,
                        value);
            } else {
                ++nullCount;
            }
        }

        if (nullCount == conditions.length) {
            throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_NO_CONDITION);
        }

        return sqlOption;
    }
}
