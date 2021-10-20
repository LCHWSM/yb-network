package com.ybau.transaction.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SHAUtil {


    /**
     * 密码加密
     */
    public final String KEY_SHA = "SHA";

    public String getResult(String inputStr) {
        BigInteger sha = null;

        byte[] inputData = inputStr.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(inputData);
            sha = new BigInteger(messageDigest.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sha.toString(32);
    }
}
