package com.finaro.payme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@ToString
@Table(name = "PaymentTransaction")
public class PaymentTransaction {
    @Id
    private Long invoice;
    private Double amount;
    private String currency;
    private String cardHolderName;
    private String email;
    private String pan;
    private Timestamp expiry;
    private String expiryString;
}
