package com.childpassmanage.net.controller;

import com.childpassmanage.net.pojo.KeyPairPO;
import com.childpassmanage.net.service.KeyPairService;
import com.childpassmanage.net.service.UserNameService;
import com.childpassmanage.net.utils.HttpUtils;
import com.childpassmanage.net.utils.PCIKeyPair;
import com.childpassmanage.net.utils.R;
import com.childpassmanage.net.utils.SM2Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;

@Controller
public class LinkUpassServerController {

    @Autowired
    private KeyPairService keyPairService;

    @Autowired
    private UserNameService userNameService;

    @RequestMapping("/getUserPublicKey")
    public R getUserPublicKey(@RequestParam("username") String username, @RequestParam("password") String password) throws InvalidAlgorithmParameterException {
        boolean userNameIfExist = userNameService.findUserNameIfExist(username);
        if(userNameIfExist){
            PCIKeyPair pciKeyPair = SM2Util.genKeyPair(false);
            KeyPairPO keyPairPO = keyPairService.selectKeyPairByUserName(username);
            if(keyPairPO != null){
                return R.ok().put("publickey",keyPairPO.getPubKey());
            }else{
                keyPairService.insertKeyPairByUsername(username,pciKeyPair.getPubKey());
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("privatekey",pciKeyPair.getPriKey());
                hashMap.put("username",username);
                HttpUtils.post("http://localhost:18088/saveUserPrivateKey",hashMap);
                return R.ok().put("publickey",pciKeyPair.getPubKey());
            }
        }else{
            return R.error();
        }
    }
}
