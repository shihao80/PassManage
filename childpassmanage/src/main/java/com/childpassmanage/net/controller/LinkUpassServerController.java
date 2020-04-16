package com.childpassmanage.net.controller;

import com.childpassmanage.net.service.UserNameService;
import com.childpassmanage.net.utils.HttpUtils;
import com.childpassmanage.net.utils.R;
import com.childpassmanage.net.utils.Sm4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class LinkUpassServerController {


    @Autowired
    private UserNameService userNameService;

    @RequestMapping("/getSessionKey")
    public R getUserPublicKey(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        boolean userNameIfExist = userNameService.findUserNameIfExist(username);
        if (userNameIfExist) {
            HashMap<String, String> hashMap = new HashMap<>();
            String sessionkey = Sm4Util.generateKey();
            hashMap.put("sessionkey", sessionkey);
            hashMap.put("username", username);
            HttpUtils.post("http://localhost:18088/saveUserSM4KEY", hashMap);
            return R.ok().put("publickey", sessionkey);
        } else {
            return R.error();
        }
    }
}
