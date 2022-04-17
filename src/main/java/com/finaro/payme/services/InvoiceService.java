package com.finaro.payme.services;

import com.finaro.payme.model.TransactionResult;
import com.finaro.payme.model.dto.PaymentTransactionDto;

public interface InvoiceService {
    TransactionResult saveInvoice(PaymentTransactionDto paymentTransactionDto);

    PaymentTransactionDto findInvoiceById(Long invoiceId);
}
