package com.bank.msyankiwalletbatch.services;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProcessWalletService {

    void process(String message) throws JsonProcessingException;
}
