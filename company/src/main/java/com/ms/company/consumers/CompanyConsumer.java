package com.ms.company.consumers;

import com.ms.company.dtos.CompanyRecordDTO;
import com.ms.company.models.CompanyModel;
import com.ms.company.services.CompanyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CompanyConsumer {

    private final CompanyService companyService;
    private final RabbitTemplate rabbitTemplate;

    private static final String BANK_EXCHANGE = "bank.exchange";
    private static final String BANK_ROUTING_KEY = "bank.create.account";

    public CompanyConsumer(CompanyService companyService, RabbitTemplate rabbitTemplate) {
        this.companyService = companyService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${broker.queue.company.name}")
    public void listenCompanyQueue(@Payload CompanyRecordDTO companyRecordDto) {
        var companyModel = new CompanyModel();
        BeanUtils.copyProperties(companyRecordDto, companyModel);
        companyService.processCompany(companyModel);

        rabbitTemplate.convertAndSend(BANK_EXCHANGE, BANK_ROUTING_KEY, companyModel.getUserId());
    }
}
