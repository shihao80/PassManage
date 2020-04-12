package com.passManage.us.admin.common.util;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * @Description:
 * @Author: 陈欢
 * @Date: 2018/10/24 09:41
 */
public class SM2KeyPair {


    /**
     * 公钥
     */
    private ECPoint publicKey;

    /**
     * 私钥
     */
    private BigInteger privateKey;

    public SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

}


