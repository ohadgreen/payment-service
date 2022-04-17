package com.finaro.payme.services.audit;

import com.finaro.payme.model.PaymentTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditServiceToFile implements AuditService{
    @Value("${audit.file.path}")
    private String filePath;

    @Override
    public void writeInvoiceAudit(PaymentTransaction paymentTransaction) {
        log.info("writing txn no. {} to file {}", paymentTransaction.getInvoice(), filePath);
//        TODO: write to file impl
    }
}
