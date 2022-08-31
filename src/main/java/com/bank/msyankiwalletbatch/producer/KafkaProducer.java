package com.bank.msyankiwalletbatch.producer;

import com.bank.msyankiwalletbatch.models.utils.DataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${kafka.topic.transactionBootCoin}")
    private String topicTransactionBootCoin;

    @Autowired
    private KafkaTemplate<String, DataEvent<?>> producer;

    public void sendMessageTransactionBootCoin(DataEvent<?> dataEvent) {
        log.info("Producing topic {}, message {}",topicTransactionBootCoin, dataEvent.toString());
        this.producer.send(topicTransactionBootCoin, dataEvent);
    }

}