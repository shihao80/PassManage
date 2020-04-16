package com.passuser.net.controller.user;

import com.google.gson.Gson;
import com.passuser.net.utils.HttpUtils;
import com.passuser.net.utils.Md5TokenGenerator;
import com.passuser.net.utils.R;
import com.passuser.net.KeyUtils.Sm4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class UserController {

    @Autowired
    Md5TokenGenerator tokenGenerator;

    @RequestMapping("/login")
    public R loginUserBySM4(@RequestParam("username")String username, @RequestParam("password")String password, HttpServletRequest request, HttpServletResponse response){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("password",password);
        String post = HttpUtils.post("http://localhost:18086/getSessionKey", hashMap);
        Gson gson = new Gson();
        R publicKey = gson.fromJson(post, R.class);
        String sessionkey = (String) publicKey.get("sessionkey");
        jedis.set("sessionkey",sessionkey);
        String jsonUserName = gson.toJson(hashMap);
        String userEncrypt = Sm4Util.encryptEcb(sessionkey, jsonUserName);
        HashMap<String,String> userInfos = new HashMap<>();
        userInfos.put("userEncrypt",userEncrypt);
        String userPost = HttpUtils.post("http://localhost:18088/remoteauth", userInfos);
        R r = gson.fromJson(userPost, R.class);
        Boolean auth =(Boolean) r.get("auth");
        if(auth){
        String token = tokenGenerator.generate(username, password);
            response.setHeader("Authorization",token);
        }
        return auth ? R.ok():R.error();
    }

}
