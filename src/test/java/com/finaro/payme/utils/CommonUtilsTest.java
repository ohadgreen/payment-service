package com.finaro.payme.utils;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilsTest {

    private CommonUtils commonUtils = new CommonUtils();

    @Test
    void expiryStringToTimestampTest() {
        String validDate = "0125";
        Timestamp timestamp = commonUtils.convertStringToTimestampWrapper(validDate);
        Month month = timestamp.toLocalDateTime().getMonth();
        System.out.println(month);
        assertEquals(Month.JANUARY, month);

        String nonValidDate = "wwww";
        Timestamp nonValidTs = commonUtils.convertStringToTimestampWrapper(nonValidDate);
        assertNull(nonValidTs);
    }

    @Test
    void emailFormatValidatorTest() {
        String validEmail = "john.doe@provider.com";
        boolean validEmailChecked = commonUtils.emailValidationWrapper(validEmail, true);
        assertTrue(validEmailChecked);

        String nonValidEmail = "abrakadabra";
        boolean nonValidEmailChecked = commonUtils.emailValidationWrapper(nonValidEmail, true);
        assertFalse(nonValidEmailChecked);
    }

    @Test
    void luhnCheck() {
        String validCardNumber="1358954993914435";
        boolean luhnCheck = commonUtils.luhnCheckWrapper(validCardNumber);
        assertTrue(luhnCheck);

        String invalidCardNumber="13589549939999";
        luhnCheck = commonUtils.luhnCheckWrapper(invalidCardNumber);
        assertFalse(luhnCheck);
    }
}