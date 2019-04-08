package com.arbfintech.microservice.loan.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.arbfintech.component.core.message.RabbitMQMessage;
import com.rabbitmq.client.Channel;


@Service
@RabbitListener(queues="lead_server.send_lead_to_contract")
public class ReceiveLeadService {
	
	@Autowired
	private LoanService loanService;

	private Logger log = LoggerFactory.getLogger(ReceiveLeadService.class);
	@RabbitHandler
    public void process(String lead, Channel channel, Message message)throws IOException {

        try{
        	
        	//告诉服务器已接受到消息，已被消费，可以在队列删除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            
            System.out.println("receive success");
            
            log.info("收到的数据lead：-----》"+JSON.parseObject(lead, RabbitMQMessage.class).getMessageData());
            Util.report(JSON.parseObject(lead, RabbitMQMessage.class).getMessageData());
            
            loanService.saveLoanContractFromLeads(JSON.parseObject(lead, RabbitMQMessage.class).getMessageData());
            
            Util.report("完成contract存储！！");
            
            
        }catch(IOException e) {
            e.printStackTrace();
            //丢弃这条消息
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");


        }
    }
	
}
