package com.bank.msyankiwalletbatch.consumer;

import com.bank.msyankiwalletbatch.services.ProcessWalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Autowired
    private ProcessWalletService processWalletService;
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "${kafka.topic.wallet}")
    public void consume(String message) {
        log.info("[INI] Consume");
        log.info("Consuming Message {}", message);

        try {
            processWalletService.process(message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("[END] Consume");
    }

}