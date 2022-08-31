package com.bank.msyankiwalletbatch.services.impl;

import com.bank.msyankiwalletbatch.constants.Constant;
import com.bank.msyankiwalletbatch.models.documents.Payment;
import com.bank.msyankiwalletbatch.models.documents.Transaction;
import com.bank.msyankiwalletbatch.models.documents.TransactionTransfer;
import com.bank.msyankiwalletbatch.models.documents.Wallet;
import com.bank.msyankiwalletbatch.models.utils.DataEvent;
import com.bank.msyankiwalletbatch.producer.KafkaProducer;
import com.bank.msyankiwalletbatch.services.WalletService;
import com.bank.msyankiwalletbatch.services.ProcessWalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ProcessWalletServiceImpl implements ProcessWalletService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final Logger log = LoggerFactory.getLogger(ProcessWalletServiceImpl.class);

    @Override
    public void process(String message) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DataEvent<?> dataEvent = objectMapper.readValue(message, new TypeReference<DataEvent<?>>() {});
        log.info("[INI] Process {}", dataEvent.getId());
        switch (dataEvent.getProcess()){
            case Constant.PROCESS_WALLET_CREATE:
                log.info("save Wallet");
                Wallet walletCreate = objectMapper.readValue(message, new TypeReference<DataEvent<Wallet>>() {})
                        .getData();
                walletService.create(walletCreate);
                break;
            case Constant.PROCESS_WALLET_PAYMENT:
                log.info("payment Wallet");
                Payment payment = objectMapper.readValue(message, new TypeReference<DataEvent<Payment>>() {})
                        .getData();
                walletService.payment(payment);
                break;
            case Constant.PROCESS_WALLET_TRANSACTION:
                log.info("transaction Wallet");
                Transaction transaction = objectMapper.readValue(message, new TypeReference<DataEvent<Transaction>>() {})
                        .getData();
                walletService.transaction(transaction);
                break;
            case Constant.PROCESS_WALLET_TRANSACTION_BOOTCOIN:
                log.info("transaction BootCoin Wallet");
                Transaction transactionBootcoin = objectMapper.readValue(message, new TypeReference<DataEvent<Transaction>>() {})
                        .getData();
                walletService.transactionBootcoin(transactionBootcoin);
                break;
            case Constant.PROCESS_WALLET_PAYMENT_BOOTCOIN:
                TransactionTransfer transactionTransfer = objectMapper.readValue(message, new TypeReference<DataEvent<TransactionTransfer>>() {})
                        .getData();

                Optional<TransactionTransfer> resultTransfer = walletService.paymentBootcoin(transactionTransfer);
                if(resultTransfer.isPresent()){
                    DataEvent<TransactionTransfer> transactionTransferDataEvent = new DataEvent<>();
                    transactionTransferDataEvent.setId(dataEvent.getId());
                    transactionTransferDataEvent.setProcess(Constant.PROCESS_BOOTCOIN_TRANSFER_YANKI_STATUS);
                    transactionTransferDataEvent.setDateEvent(LocalDateTime.now());
                    transactionTransferDataEvent.setData(transactionTransfer);
                    kafkaProducer.sendMessageTransactionBootCoin(transactionTransferDataEvent);
                }
                break;
            default:
                log.info("Procces Invalid {}", dataEvent.getProcess());
                break;
        }

        log.info("[END] Process {}", dataEvent.getId());
    }

}
