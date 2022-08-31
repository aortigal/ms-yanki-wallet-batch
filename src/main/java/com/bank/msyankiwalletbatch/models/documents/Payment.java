package com.bank.msyankiwalletbatch.models.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    private String recipientName;

    private String recipientPhone;

    private String comissionAmount;

    private BigDecimal amount;

    private String customerPhone;

}
