package com.bank.msyankiwalletbatch.services;

import com.bank.msyankiwalletbatch.models.documents.Payment;
import com.bank.msyankiwalletbatch.models.documents.Transaction;
import com.bank.msyankiwalletbatch.models.documents.TransactionTransfer;
import com.bank.msyankiwalletbatch.models.documents.Wallet;

import java.util.Optional;

public interface WalletService {
    Optional<Wallet> create(Wallet wallet);

    Optional<Wallet> update(Wallet wallet);

    Optional<Payment> payment(Payment payment);

    Optional<Transaction> transaction(Transaction transaction);

    String transactionBootcoin(Transaction transaction);

    Optional<Wallet> findByPhoneNumber(String phoneNumber);

    Optional<TransactionTransfer> paymentBootcoin(TransactionTransfer transactionTransfer);
}
