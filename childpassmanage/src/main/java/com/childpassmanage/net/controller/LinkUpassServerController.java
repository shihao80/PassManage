package com.childpassmanage.net.controller;

import com.childpassmanage.net.dao.KeyPassMapper;
import com.childpassmanage.net.pojo.KeyPassPO;
import com.childpassmanage.net.pojo.UserNamePO;
import com.childpassmanage.net.service.KeyPassServie;
import com.childpassmanage.net.service.SM4PubService;
import com.childpassmanage.net.service.UserNameService;
import com.childpassmanage.net.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LinkUpassServerController {


    @Autowired
    private UserNameService userNameService;

    @Autowired
    private KeyPassMapper keyPassMapper;

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

    @RequestMapping("/remote/savekeypass/{username}")
    @ResponseBody
    public R saveKeyPass(@PathVariable("username")String username,@RequestParam("keyPass")String keyPass){
        KeyPassPO keyPassPO = new KeyPassPO();
        Map<String, Long> split = Shamir.split(Integer.parseInt(keyPass));
        keyPassPO.setDian0(split.get("dian[0]"));
        keyPassPO.setDian1(split.get("dian[1]"));
        keyPassPO.setDian2(split.get("dian[2]"));
        keyPassPO.setF0(split.get("f[0]"));
        keyPassPO.setF1(split.get("f[1]"));
        keyPassPO.setF2(split.get("f[2]"));
        keyPassPO.setP(split.get("p"));
        keyPassPO.setUsername(username);
        keyPassMapper.insert(keyPassPO);
        return R.ok().put("flag", "true");
    }

    @RequestMapping("/remote/getKeyPass/{username}")
    @ResponseBody
    public R getKeyPass(@PathVariable("username")String username){
        KeyPassPO keyPassPO = new KeyPassPO();
        keyPassPO.setUsername(username);
        List<KeyPassPO> select = keyPassMapper.select(keyPassPO);
        KeyPassPO keyPassPO1 = select.get(0);
        Map<String ,Long> hashMap = new HashMap<>();
        hashMap.put("dian[0]", keyPassPO1.getDian0());
        hashMap.put("dian[1]", keyPassPO1.getDian1());
        hashMap.put("dian[2]", keyPassPO1.getDian2());
        hashMap.put("f[0]", keyPassPO1.getF0());
        hashMap.put("f[1]", keyPassPO1.getF1());
        hashMap.put("f[2]", keyPassPO1.getF2());
        hashMap.put("p", keyPassPO1.getP());
        long combine = Shamir.combine(hashMap);
        String keyByPass = ThroughUserkey.getKeyByPass(combine+"");
        return R.ok().put("keyPass",keyByPass);
    }

}
