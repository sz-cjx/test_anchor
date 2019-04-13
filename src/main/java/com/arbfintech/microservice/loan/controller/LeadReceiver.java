package com.arbfintech.microservice.loan.controller;

import com.alibaba.fastjson.JSON;
import com.arbfintech.component.core.constant.RabbitOperationConst;
import com.arbfintech.component.core.constant.RabbitProducerConst;
import com.arbfintech.component.core.message.RabbitMessage;
import com.arbfintech.microservice.loan.service.LoanService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Richard
 */

@Service
@RabbitListener(queues= RabbitProducerConst.MICROSERVICE_LEAD+"."+ RabbitOperationConst.SEND_LEAD_TO_LOAN)
public class LeadReceiver {

    private Logger logger = LoggerFactory.getLogger(LeadReceiver.class);

	@Autowired
	private LoanService loanService;

	@RabbitHandler
    public void process(String lead, Channel channel, Message message){
        try{
        	//告诉服务器已接受到消息，已被消费，可以在队列删除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            logger.info("receive lead Data: {}", JSON.parseObject(lead, RabbitMessage.class).getMessageData());
            loanService.saveLoanFromLead(JSON.parseObject(lead, RabbitMessage.class).getMessageData());
        }catch(IOException e) {
            e.printStackTrace();
            logger.error("Handle lead data failed: ", e);
        }
    }
}
