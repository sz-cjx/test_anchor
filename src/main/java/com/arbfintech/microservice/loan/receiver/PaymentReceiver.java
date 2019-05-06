package com.arbfintech.microservice.loan.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.component.core.constant.RabbitOperationConst;
import com.arbfintech.component.core.constant.RabbitProducerConst;
import com.arbfintech.component.core.enumerate.LoanStatusEnum;
import com.arbfintech.component.core.message.RabbitMessage;
import com.arbfintech.microservice.loan.receiver.LeadReceiver;
import com.arbfintech.microservice.loan.entity.Loan;
import com.arbfintech.microservice.loan.repository.LoanRepository;
import com.arbfintech.microservice.loan.service.TimeLineApiService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = RabbitProducerConst.MICROSERVICE_PAYMENT + "." + RabbitOperationConst.SEND_PAYMENT_TO_LOAN)
public class PaymentReceiver {

    private Logger logger = LoggerFactory.getLogger(LeadReceiver.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TimeLineApiService timeLineApiService;

    @RabbitHandler
    public void process(String content, Channel channel, Message message) {
        try {
            //告诉服务器已接受到消息，已被消费，可以在队列删除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info("receive payment data : {}", JSON.parseObject(content, RabbitMessage.class).getMessageData());

            String contractNos = JSON.parseObject(content, RabbitMessage.class).getMessageData();
            JSONArray contractArr = JSONArray.parseArray(contractNos);
            if (channel != null && contractArr.size() != 0) {
                for (Object contractNo : contractArr) {
                    Loan loan=loanRepository.findByContractNo((String) contractNo);

                    if (loan!=null) {
                        loan.setLoanStatus(LoanStatusEnum.NEW.getValue());
                        loanRepository.save(loan);
                        JSONObject additionData = new JSONObject();
                        additionData.put("contractNo", contractNo);
                        timeLineApiService.addLoanStatusChangeTimeline(LoanStatusEnum.APPROVED.getValue(), LoanStatusEnum.NEW.getValue(), JSONObject.toJSONString(additionData));
                    }else {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Handle lead data failed: ", e);
        }
    }
}


