package com.childpassmanage.net.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class Generatingkey {
    /**
     随机生成AES密钥
     * @return
     */
    public static String getAESKey() {
        String s1;
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kg.init(128);
        //要生成多少位，只需要修改这里即可128, 192或256
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        s1 = byteToHexString(b);
        //     System.out.println("随机生成的密钥长度:"+s1.length()*4);
        //    System.out.println("密钥:"+s1);
        return s1;
    }

    public static String getDESKey() {
        String s1;
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kg.init(56);
        //要生成多少位，只需要修改这里即可128, 192或256
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        s1 = byteToHexString(b);
        //     System.out.println("随机生成的密钥长度:"+s1.length()*4);
        //    System.out.println("密钥:"+s1);

        return s1;
    }

    /**
     byte数组转化为16进制字符串
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
}
