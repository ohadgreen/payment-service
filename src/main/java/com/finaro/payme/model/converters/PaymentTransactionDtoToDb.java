package com.finaro.payme.model.converters;

import com.finaro.payme.model.PaymentTransaction;
import com.finaro.payme.model.dto.PaymentTransactionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionDtoToDb implements Converter<PaymentTransactionDto, PaymentTransaction> {
    @Override
    public PaymentTransaction convert(PaymentTransactionDto source) {

//        if (allFieldsInReceivedTxnExist(source)) {
//            PaymentTransaction.builder()
//                    .invoiceId(source.getInvoiceId())
//                    .amount(source.getAmount())
//        }
//
        return null;
    }

    private boolean allFieldsInReceivedTxnExist(PaymentTransactionDto paymentTransactionDto) {
        return paymentTransactionDto.getInvoice() != null
                && paymentTransactionDto.getAmount() != null
                && paymentTransactionDto.getCurrency() != null
                && paymentTransactionDto.getCardholderDto() != null
                && paymentTransactionDto.getCreditCardDto() != null
                && paymentTransactionDto.getCardholderDto().getName() != null
                && paymentTransactionDto.getCardholderDto().getEmail() != null;
    }
}
