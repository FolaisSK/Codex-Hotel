package org.fola.utils;

import java.security.SecureRandom;

public class ReferenceNumberGenerator {
    private static final String DIGITS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRefNo(int noOfDigits){
        StringBuilder code = new StringBuilder();

        for(int index = 0; index < noOfDigits; index++){
            int digit = random.nextInt(DIGITS.length());
            code.append(digit);
        }
        return "RES" + code;
    }
}
