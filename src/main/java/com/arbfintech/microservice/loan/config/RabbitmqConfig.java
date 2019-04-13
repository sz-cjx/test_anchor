package com.arbfintech.microservice.loan.config;

import com.arbfintech.component.core.constant.RabbitExchangeConst;
import com.arbfintech.component.core.constant.RabbitOperationConst;
import com.arbfintech.component.core.constant.RabbitProducerConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue queueMessageContract() {
        return new Queue(RabbitProducerConst.MICROSERVICE_LOAN+"."+ RabbitOperationConst.SEND_LOAN_TO_PAYMENT);
    }

    @Bean
    DirectExchange exchangeContract() {
        return new DirectExchange(RabbitExchangeConst.EXCHANGE_LOAN);
    }

    @Bean
    Binding bindingDirectExchangeMessageContract(Queue queueMessageContract, DirectExchange exchangeContract) {
        return BindingBuilder.bind(queueMessageContract).to(exchangeContract).with(RabbitProducerConst.MICROSERVICE_LOAN+"."+ RabbitOperationConst.SEND_LOAN_TO_PAYMENT);

    }
}
