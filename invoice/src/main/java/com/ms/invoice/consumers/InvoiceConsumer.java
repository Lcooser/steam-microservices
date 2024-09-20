package com.ms.invoice.consumers;

import com.ms.invoice.dtos.*;
import com.ms.invoice.models.InvoiceModel;
import com.ms.invoice.services.InvoiceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@Component
public class InvoiceConsumer {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String COMPANY_SERVICE_URL = "http://localhost:8086/company/";
    private static final String PLAYER_SERVICE_URL = "http://localhost:8083/players/userId/";
    private static final String GAME_SERVICE_URL = "http://localhost:8082/games/";

    @RabbitListener(queues = "${broker.queue.invoice.name}")
    public void handleInvoiceMessage(OrderDTO orderDTO) {
        InvoiceModel invoice = new InvoiceModel();
        invoice.setOrderId(orderDTO.getOrderId());
        invoice.setUserId(orderDTO.getUserId());
        invoice.setGameId(orderDTO.getGameId());
        invoice.setPaymentDate(orderDTO.getPaymentDate());
        invoice.setQuantity(orderDTO.getQuantity());
        invoice.setAmount(orderDTO.getAmount());

        enrichInvoiceWithAdditionalData(invoice, orderDTO.getGameId(), orderDTO.getUserId());

        invoiceService.save(invoice);
    }

    private void enrichInvoiceWithAdditionalData(InvoiceModel invoice, UUID gameId, UUID userId) {
        ResponseEntity<PlayerDTO> playerResponse = restTemplate.getForEntity(PLAYER_SERVICE_URL + userId, PlayerDTO.class);
        if (playerResponse.getStatusCode() == HttpStatus.OK) {
            PlayerDTO player = playerResponse.getBody();
            invoice.setPlayerCpf(player.getCpf());
            invoice.setPlayerName(player.getName());
            invoice.setPlayerEmail(player.getEmail());
            invoice.setPlayerPhone(player.getPhone());
        }

        ResponseEntity<GameDTO> gameResponse = restTemplate.getForEntity(GAME_SERVICE_URL + gameId, GameDTO.class);
        if (gameResponse.getStatusCode() == HttpStatus.OK) {
            GameDTO game = gameResponse.getBody();
            invoice.setGameName(game.getGameName());
            UUID companyId = game.getCompanyId();
            ResponseEntity<CompanyDTO> companyResponse = restTemplate.getForEntity(COMPANY_SERVICE_URL + companyId, CompanyDTO.class);
            if (companyResponse.getStatusCode() == HttpStatus.OK) {
                CompanyDTO company = companyResponse.getBody();
                invoice.setCompanyCnpj(company.getCnpj());
                invoice.setCompanyName(company.getName());
            }
        }
    }

}
