package com.passManage.us.admin.common.KeyUtils;


import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * @author CHENR
 * @Title: Sm3Utils
 * @ProjectName upds
 * @Description: TODO
 * @date 2019/9/611:13
 */
public class Sm3Utils {
    private final static byte[] hex = "0123456789ABCDEF".getBytes();
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;
    public static final String ALGORITHM_NAME = "SM3";

    /**
     * 自动生成密钥
     *
     * @return
     * @explain
     */
    public static String generateKey() throws Exception {
        return new String(Hex.encodeHex(generateKey(DEFAULT_KEY_SIZE),false));
    }
    /**
     * @desc SM3加密算法实现方法
     * @param data
     * @return
     */
    public static byte[] digest(byte[] data) {
        SM3Digest sm3 = new SM3Digest();
        byte[] result = null;
        try {
            sm3.update(data, 0, data.length);
            result = new byte[sm3.getDigestSize()];
            sm3.doFinal(result, 0);
        } catch (Exception e) {
            System.out.println("Fail: SM3 byte[] to byte[]" + e);
        }
        return result;
    }

    /**
     * @desc 将字节数组转换为16进制的字符串
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }

    /**
     * @desc 调用该方法使用SM3算法对手机号、卡号、证件号进行加密，输出16进制的密文字符串
     * @param msg  要加密的信息
     * @return  16进制的加密密文字符串，如加密算法调用失败返回空串
     */
    public static String encryptBySm3(String msg) {
        try {
            String encryptedCardNo = bytes2HexString(digest(msg.getBytes("UTF-8")));
            return encryptedCardNo;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param keySize
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

}
