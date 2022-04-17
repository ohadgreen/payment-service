package com.finaro.payme.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class CommonUtils {
    public static boolean isValidEmailAddress(String email, boolean allowLocal) {
        return EmailValidator.getInstance(allowLocal).isValid(email);
    }

    public static Timestamp convertStringToTimestamp(String strDate) {
//        TODO: extract "20" and "MMyyyy" to props file
        if (strDate.length() < 4) {
            return null;
        }

        char[] expiryCharArray = strDate.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(expiryCharArray[0]);
        stringBuilder.append(expiryCharArray[1]);
        stringBuilder.append("20");
        stringBuilder.append(expiryCharArray[2]);
        stringBuilder.append(expiryCharArray[3]);

        try {
            DateFormat formatter = new SimpleDateFormat("MMyyyy");
            Date date = formatter.parse(stringBuilder.toString());
            Timestamp expiryTimestamp = new Timestamp(date.getTime());
            if (expiryTimestamp.before(Timestamp.valueOf(LocalDateTime.now()))) {
                log.error("Card expired");
                return null;
            }

            return expiryTimestamp;
        } catch (ParseException e) {
            log.error("Expiry time parse exception :" + e);
            return null;
        }
    }

    public static boolean checkLuhn(String cardNo)
    {
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {
            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    public static String base64Encoder(String rawString) {
        try {
            byte[] bytes = rawString.getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (Exception e) {
            log.error("encoding error");
            return null;
        }
    }

    protected boolean emailValidationWrapper(String email, boolean allowLocal) {
        return isValidEmailAddress(email, allowLocal);
    }

    protected Timestamp convertStringToTimestampWrapper(String strDate) {
        return convertStringToTimestamp(strDate);
    }

    protected boolean luhnCheckWrapper(String cardNumber) {
        return checkLuhn(cardNumber);
    }
}
