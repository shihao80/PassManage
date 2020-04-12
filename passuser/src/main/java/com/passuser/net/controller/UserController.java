package com.passuser.net.controller;

import com.alibaba.dubbo.common.json.JSONObject;
import com.google.gson.Gson;
import com.passuser.net.utils.HttpUtils;
import com.passuser.net.utils.R;
import com.passuser.net.utils.SM2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class UserController {

    @RequestMapping("/login")
    public R loginUserBySM2(@RequestParam("username")String username, @RequestParam("password")String password){
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("password",password);
        String post = HttpUtils.post("http://localhost:18086/getUserPublicKey", hashMap);
        Gson gson = new Gson();
        R publicKey = gson.fromJson(post, R.class);
        String publickey = (String) publicKey.get("publickey");
        String jsonUserName = gson.toJson(hashMap);
        String userEncrypt = SM2Util.encrypt(publickey, jsonUserName);
        HashMap<String,String> userInfos = new HashMap<>();
        userInfos.put("userEncrypt",userEncrypt);
        String userPost = HttpUtils.post("http://localhost:18088/remoteauth", userInfos);
        R r = gson.fromJson(userPost, R.class);
        Boolean auth =(Boolean) r.get("auth");
        return auth?R.ok():R.error();
    }
}
