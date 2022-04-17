package com.finaro.payme.services;

import com.finaro.payme.model.PaymentTransaction;
import com.finaro.payme.model.dto.CardholderDto;
import com.finaro.payme.model.dto.CreditCardDto;
import com.finaro.payme.model.dto.PaymentTransactionDto;
import com.finaro.payme.model.TransactionResult;
import com.finaro.payme.repositories.InvoiceRepository;
import com.finaro.payme.services.audit.AuditService;
import com.finaro.payme.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService{

    private final InvoiceRepository invoiceRepository;
    private final AuditService auditService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, AuditService auditService) {
        this.invoiceRepository = invoiceRepository;
        this.auditService = auditService;
    }

    public TransactionResult saveInvoice(PaymentTransactionDto paymentTransactionDto) {
        TransactionResult transactionResult = new TransactionResult();
        Map<String, String> transactionValidationErrors = new HashMap<>();
        PaymentTransaction paymentTransaction = incomingInvoiceValidatorAndConverter(paymentTransactionDto, transactionValidationErrors);
        log.debug(String.valueOf(paymentTransaction));

        if (transactionValidationErrors.size() > 0) {
            transactionResult.setApproved(false);
            transactionResult.setErrors(transactionValidationErrors);
        } else {
            PaymentTransaction savedTransaction = invoiceRepository.save(paymentTransaction);
            log.debug(String.valueOf(savedTransaction));
            transactionResult.setApproved(true);
            auditService.writeInvoiceAudit(paymentTransaction);
        }

        return transactionResult;
    }

    protected PaymentTransaction incomingInvoiceValidatorAndConverter(PaymentTransactionDto paymentTransactionDto, Map<String, String> transactionValidationErrors) {

        if (paymentTransactionDto.getCardholderDto() == null || paymentTransactionDto.getCreditCardDto() == null) {
            transactionValidationErrors.put("credit card info", "missing values");
            return null;
        }

        PaymentTransaction paymentTransaction = new PaymentTransaction();

        if (paymentTransactionDto.getAmount() == null || paymentTransactionDto.getAmount() <= 0) {
            transactionValidationErrors.put("amount", "invalid amount");
        } else {
            paymentTransaction.setAmount(paymentTransactionDto.getAmount());
        }

        if (paymentTransactionDto.getCreditCardDto().getPan() == null || CommonUtils.checkLuhn(paymentTransactionDto.getCreditCardDto().getPan())) {
            transactionValidationErrors.put("pan", "invalid card number");
        } else {

            String rawPanString = paymentTransactionDto.getCreditCardDto().getPan();
            if (rawPanString.length() > 4) {
                String panToMask = rawPanString.substring(0, rawPanString.length() - 4);
                String panLast4Digits = rawPanString.substring(rawPanString.length() - 4);
                paymentTransaction.setPan(Objects.requireNonNull(CommonUtils.base64Encoder(panToMask)).concat(panLast4Digits));
            }
        }

        if (paymentTransactionDto.getCreditCardDto().getExpiry() != null) {
            Timestamp timestamp = CommonUtils.convertStringToTimestamp(paymentTransactionDto.getCreditCardDto().getExpiry());
            if (timestamp != null) {
                paymentTransaction.setExpiry(timestamp);
                paymentTransaction.setExpiryString(CommonUtils.base64Encoder(paymentTransactionDto.getCreditCardDto().getExpiry()));
            } else {
                transactionValidationErrors.put("expiry", "Payment card is expired");
            }
        }

        if (paymentTransactionDto.getCardholderDto().getEmail() == null || !CommonUtils.isValidEmailAddress(paymentTransactionDto.getCardholderDto().getEmail(), true)) {
            transactionValidationErrors.put("email", "Invalid cardholder email format.");
        } else {
            paymentTransaction.setEmail(paymentTransactionDto.getCardholderDto().getEmail());
        }
//        TODO: check the values below exist
        paymentTransaction.setInvoice(paymentTransactionDto.getInvoice());
        paymentTransaction.setCurrency(paymentTransactionDto.getCurrency());
        paymentTransaction.setCardHolderName(CommonUtils.base64Encoder(paymentTransactionDto.getCardholderDto().getName()));

        return paymentTransaction;
    }

    public PaymentTransactionDto findInvoiceById(Long invoiceId) {
        PaymentTransaction paymentTransaction = invoiceRepository.findByInvoice(invoiceId);
        if (paymentTransaction != null) {
            return paymentTransactionToDtoConverter(paymentTransaction);
        }
        return null;
    }

    protected PaymentTransactionDto paymentTransactionToDtoConverter(PaymentTransaction paymentTransaction) {
        return PaymentTransactionDto.builder()
                .invoice(paymentTransaction.getInvoice())
                .amount(paymentTransaction.getAmount())
                .currency(paymentTransaction.getCurrency())
                .cardholderDto(CardholderDto.builder()
                        .email(paymentTransaction.getEmail())
                        .name(paymentTransaction.getCardHolderName())
                        .build())
                .creditCardDto(CreditCardDto.builder()
                        .pan(paymentTransaction.getPan())
                        .expiry(paymentTransaction.getExpiryString())
                        .build())
                .build();
    }
}
