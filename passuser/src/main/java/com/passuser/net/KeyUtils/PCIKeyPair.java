package com.passuser.net.KeyUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PCIKeyPair {

    private String priKey;      //私钥
    private String pubKey;      //公钥
    private String username;    //用户名

    public PCIKeyPair(String priKey, String pubKey) {
        this.priKey = priKey;
        this.pubKey = pubKey;
    }
}