package com.passManage.us.admin.common.KeyUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PCIKeyPair {

    private String priKey;      //私钥
    private String pubKey;      //公钥

}
