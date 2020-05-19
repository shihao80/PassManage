package com.childpassmanage.net.controller;

import com.childpassmanage.net.pojo.UserNamePO;
import com.childpassmanage.net.service.SM4PubService;
import com.childpassmanage.net.service.UserNameService;
import com.childpassmanage.net.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@Controller
public class LinkUpassServerController {


    @Autowired
    private UserNameService userNameService;

    @Autowired
    private SM4PubService sm4PubService;

    @RequestMapping(value = "/remote/getSessionKey",method = RequestMethod.POST)
    @ResponseBody
    public R getUserPublicKey(@RequestParam("username")String username,@RequestParam("password")String password) throws Exception {
        boolean userNameIfExist = userNameService.findUserNameIfExist(username);
        if (userNameIfExist) {
            HashMap<String, String> hashMap = new HashMap<>();
            String sessionkey = Sm4Util.generateKey();
            hashMap.put("sessionkey", sessionkey);
            hashMap.put("username", username);
            HttpUtils.post("http://localhost:18088/us-admin/remote/saveUserSM4KEY", hashMap);
            return R.ok().put("sessionkey", sessionkey);
        } else {
            return R.error();
        }
    }

    @RequestMapping("/remote/setUserName/{username}")
    public void setUserName(@PathVariable("username")String userName) throws Exception {
        PCIKeyPair pciKeyPair = SM2Util.genKeyPair();
        String priKey = pciKeyPair.getPriKey();
        String pubKey = pciKeyPair.getPubKey();
        sm4PubService.insertPubKey(pubKey,userName);
        UserNamePO userNamePO = new UserNamePO();
        userNamePO.setUserName(userName);
        userNamePO.setCreateDate(new Date());
        userNamePO.setIfClose(0);
        userNamePO.setPriKey(priKey);
        userNameService.insertUserName(userNamePO);
    }

    @RequestMapping("/remote/getSM2Pub/{username}")
    @ResponseBody
    public R getSM4Pub(@PathVariable("username")String username){
        String pubKey =sm4PubService.getSM4PubByUserName(username);
        return R.ok().put("pubKey", pubKey);
    }

    @RequestMapping("/remote/getSM2Pri/{username}")
    @ResponseBody
    public R getSM4Pri(@PathVariable("username")String username){
        String pubKey =userNameService.getSM4PriByUserName(username);
        return R.ok().put("priKey", pubKey);
    }
}
