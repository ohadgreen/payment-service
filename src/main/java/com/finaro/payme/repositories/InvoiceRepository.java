package com.finaro.payme.repositories;

import com.finaro.payme.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<PaymentTransaction, Long> {
    PaymentTransaction findByInvoice(Long invoiceId);
}
