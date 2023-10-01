package com.group77.fitness.fitnesscore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordMD5 {
    public static String getMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");//获取MD5实例
            byte[] digest = md.digest(plainText.getBytes());
            StringBuilder result = new StringBuilder();

            for(byte b : digest)
            {
                String temp = Integer.toHexString(b & 0xff);
                if(temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
