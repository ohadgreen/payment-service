package com.finaro.payme.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionDto {
    private Long invoice;
    private Double amount;
    private String currency;
    @JsonProperty("cardholder")
    private CardholderDto cardholderDto;
    @JsonProperty("card")
    private CreditCardDto creditCardDto;
}
