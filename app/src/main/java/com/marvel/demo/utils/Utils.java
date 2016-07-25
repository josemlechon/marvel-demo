package com.marvel.demo.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jose m lechon on 25/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class Utils {


    public static String md5(final String s) {
        String res = null;
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            final StringBuilder hexString = new StringBuilder();

            for (byte aMessageDigest : messageDigest) {
                int item = (aMessageDigest & 0xFF);

                if (item < 0x10) {
                    hexString.append("0");
                }
                hexString.append(Integer.toHexString(item));
            }
            res = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static Double convertDouble(String value) {
        Double valueDouble = 0d;
        try {

            value = removeNonDecimal(value);
            valueDouble = Double.parseDouble(value);

        } catch (Exception e) {
        }

        return valueDouble;
    }


    public static BigDecimal convertBigDecimal(String value) {

        try {
            value = removeNonDecimal(value);
            return new BigDecimal(value);
        } catch (Exception e) {
        }

        return new BigDecimal(0);
    }

    /**
     * Remove anything from the String that is not a number a dor or a coma.
     * Reg Ex:  /[^a-z0-9-]/g
     */
    public static String removeNonDecimal(String value) {

        if (isStringEmpty(value)) return "";
        try {
            String regex = "[^0-9.,]";

            return value.replaceAll(regex, "");
        } catch (Exception e) {
            return "";
        }

    }

    public static boolean isStringEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
