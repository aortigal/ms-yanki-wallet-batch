package com.bank.msyankiwalletbatch.models.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String senderName;

    private String senderPhone;

    private BigDecimal amount;

}
