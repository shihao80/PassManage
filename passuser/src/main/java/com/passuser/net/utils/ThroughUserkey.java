package com.passuser.net.utils;/*
主密钥生成类
*/
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

public class ThroughUserkey {

    /**
     随机生成密钥
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
     使用指定的字符串生成密钥
     * @return
     */
    public static String getKeyByPass(String password) {
        //生成秘钥
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
        //System.out.println("输入指定字符串生成的密钥的长度:");
        //int i=input.nextInt();

        kg.init(128, new SecureRandom(password.getBytes()));
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        String s2 = byteToHexString(b);
        // System.out.println("指定字符串生成的密钥长度:"+s2.length()*4);
        //  System.out.println("密钥:"+s2);
        return s2;
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
