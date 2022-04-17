package com.finaro.payme.services;

import com.finaro.payme.model.PaymentTransaction;
import com.finaro.payme.model.dto.PaymentTransactionDto;
import com.finaro.payme.repositories.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    InvoiceRepository invoiceRepository;
    @InjectMocks
    InvoiceServiceImpl invoiceService;

    @Test
    void paymentTransactionToDtoConverterTest() {
        Double amount = 1000D;
        PaymentTransaction txnTest = PaymentTransaction.builder()
                .invoice(1L)
                .amount(amount)
                .currency("USD")
                .email("john.doe@acme.com")
                .cardHolderName("John Doe")
                .pan("1358954993914435")
                .expiryString("0130")
                .build();

        PaymentTransactionDto paymentTransactionDto = invoiceService.paymentTransactionToDtoConverter(txnTest);
        assertEquals(amount, paymentTransactionDto.getAmount());
    }
}