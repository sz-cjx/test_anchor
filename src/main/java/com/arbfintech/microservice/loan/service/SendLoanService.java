package com.arbfintech.microservice.loan.service;

import com.alibaba.fastjson.JSON;
import com.arbfintech.framework.component.core.constant.RabbitExchangeConst;
import com.arbfintech.framework.component.core.constant.RabbitOperationConst;
import com.arbfintech.framework.component.core.constant.RabbitProducerConst;
import com.arbfintech.framework.component.core.type.RabbitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendLoanService implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback{

    private static final Logger LOG = LoggerFactory.getLogger(SendLoanService.class);

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(RabbitMessage content) {
        this.rabbitTemplate.setReturnCallback(this);  //回调
        //获取到回调的
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                LOG.info("HelloSender消息发送失败" + cause + correlationData.toString());
            } else {
                LOG.info("消息发送成功 ");
            }
        });
        this.rabbitmqTemplate.convertAndSend(RabbitExchangeConst.EXCHANGE_LOAN, RabbitProducerConst.MICROSERVICE_LOAN+"."+ RabbitOperationConst.SEND_LOAN_TO_PAYMENT, JSON.toJSONString(content));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {

    }
}
