package com.finaro.payme.controllers;

import com.finaro.payme.model.TransactionResult;
import com.finaro.payme.model.dto.PaymentTransactionDto;
import com.finaro.payme.services.InvoiceService;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/")
public class PaymentTransactionController {
    private final InvoiceService invoiceService;

    public PaymentTransactionController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("payment")
    public ResponseEntity<TransactionResult> savePaymentTransaction(@RequestBody PaymentTransactionDto paymentTransactionDto) {

        log.debug(paymentTransactionDto.toString());
        TransactionResult transactionResult = invoiceService.saveInvoice(paymentTransactionDto);
        if (transactionResult.getApproved()) {
            return new ResponseEntity<>(transactionResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(transactionResult, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("payment/{invoiceId}")
    public ResponseEntity<PaymentTransactionDto> getPaymentTransaction(@PathVariable("invoiceId") @NotNull String invoiceId) {
        PaymentTransactionDto paymentByInvoiceId = invoiceService.findPaymentByInvoiceId(Long.valueOf(invoiceId));
        if (paymentByInvoiceId == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentByInvoiceId, HttpStatus.OK);
    }
}
