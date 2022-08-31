package com.bank.msyankiwalletbatch.models.dao;

import com.bank.msyankiwalletbatch.models.documents.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentDao extends MongoRepository<Wallet, String> {
}
