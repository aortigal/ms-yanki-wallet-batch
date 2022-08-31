package com.bank.msyankiwalletbatch.services.impl;

import com.bank.msyankiwalletbatch.models.dao.PaymentDao;
import com.bank.msyankiwalletbatch.models.documents.Payment;
import com.bank.msyankiwalletbatch.models.documents.Transaction;
import com.bank.msyankiwalletbatch.models.documents.TransactionTransfer;
import com.bank.msyankiwalletbatch.models.documents.Wallet;
import com.bank.msyankiwalletbatch.models.enums.StatusEnum;
import com.bank.msyankiwalletbatch.models.utils.Status;
import com.bank.msyankiwalletbatch.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private PaymentDao dao;

    private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Override
    public Optional<Wallet> create(Wallet wallet) {
        wallet.setDateRegister(LocalDateTime.now());
        return Optional.of(dao.save(wallet));
    }

    @Override
    public Optional<Wallet> update(Wallet wallet) {
        wallet.setDateUpdate(LocalDateTime.now());
        return Optional.of(dao.save(wallet));
    }

    @Override
    public Optional<Payment> payment(Payment payment) {
        Optional<Wallet> wallet = findByPhoneNumber(payment.getRecipientPhone());
        if (wallet.isPresent()){
            wallet.get().setAmount(wallet.get().getAmount().subtract(payment.getAmount()));
            if(wallet.get().getAmount().signum() == -1){
                log.info("operation is negative {}", wallet.get().getAmount());
                return Optional.empty();
            }else{
                log.info("update wallet id {}", wallet.get().getId());
                update(wallet.get());
                return Optional.of(payment);

            }
        }
        log.info("wallet is empty");
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> transaction(Transaction transaction) {
        Optional<Wallet> wallet = findByPhoneNumber(transaction.getSenderPhone());
        if (wallet.isPresent()){
            wallet.get().setAmount(wallet.get().getAmount().add(transaction.getAmount()));
            log.info("update wallet id {}", wallet.get().getId());
            update(wallet.get());
            return Optional.of(transaction);
        }
        log.info("wallet is empty");
        return Optional.empty();
    }

    @Override
    public String transactionBootcoin(Transaction transaction) {
        Optional<Wallet> wallet = findByPhoneNumber(transaction.getSenderPhone());
        if (wallet.isPresent()){
            wallet.get().setAmount(wallet.get().getAmount().add(transaction.getAmount()));
            log.info("update wallet id {}", wallet.get().getId());
            update(wallet.get());
            return "Success";
        }
        log.info("wallet is empty");
        return "wallet is empty";
    }

    @Override
    public Optional<Wallet> findByPhoneNumber(String phoneNumber) {
        log.info("findByPhoneNumber {}", phoneNumber);
        return dao.findAll()
                .stream()
                .filter(x -> x.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

    @Override
    public Optional<TransactionTransfer> paymentBootcoin(TransactionTransfer transactionTransfer) {
        Optional<Wallet> wallet = findByPhoneNumber(transactionTransfer.getSenderAccount());
        if (wallet.isPresent()){
            wallet.get().setAmount(wallet.get().getAmount().subtract(transactionTransfer.getAmount()));
            if(wallet.get().getAmount().signum() == -1){
                log.info("operation is negative {}", wallet.get().getAmount());
                transactionTransfer.setStatus(new Status(StatusEnum.ERROR.getCode(),"Insufficient amount."));
                return Optional.of(transactionTransfer);
            }else{
                log.info("update wallet id {}", wallet.get().getId());
                update(wallet.get());
                transactionTransfer.setStatus(new Status(StatusEnum.SUCCESS.getCode(),StatusEnum.SUCCESS.getDescription()));
                return Optional.of(transactionTransfer);
            }
        }
        log.info("wallet is empty");
        transactionTransfer.setStatus(new Status(StatusEnum.ERROR.getCode(),"Wallet is empty."));
        return Optional.of(transactionTransfer);
    }

}
