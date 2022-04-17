package com.finaro.payme.services.audit;

import com.finaro.payme.model.PaymentTransaction;

public interface AuditService {
    void writeInvoiceAudit(PaymentTransaction paymentTransaction);
}
