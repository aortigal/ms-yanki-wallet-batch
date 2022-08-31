package com.bank.msyankiwalletbatch.models.documents;

import com.bank.msyankiwalletbatch.models.utils.Audit;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "wallets")
public class Wallet extends Audit {

    @Id
    private String id;

    private String phoneNumber;

    private BigDecimal amount;

}
