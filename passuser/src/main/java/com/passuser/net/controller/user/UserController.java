package com.passuser.net.controller.user;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import com.passuser.net.KeyUtils.Sm4Util;
import com.passuser.net.utils.HttpUtils;
import com.passuser.net.utils.Md5TokenGenerator;
import com.passuser.net.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Resource
    Md5TokenGenerator tokenGenerator;

    @Autowired
    private RedisTemplate<String, String> redisUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUserBySM4(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, String> hashMap = new HashMap<>();
        Gson gson = new Gson();
        hashMap.put("username", username);
        hashMap.put("password", password);
        String post = HttpUtils.post("http://localhost:18087/remote/getSessionKey", hashMap);
        Map<String ,Object> map = new HashMap<>();
        Map<String ,Object> publicKey = gson.fromJson(post, map.getClass());
        String sessionkey = (String) publicKey.get("sessionkey");
        redisUtil.opsForValue().set("sessionkey", sessionkey);
        String jsonUserName = gson.toJson(hashMap);
        String userEncrypt = Sm4Util.encryptEcb(sessionkey, jsonUserName);
        HashMap<String, String> userInfos = new HashMap<>();
        userInfos.put("username", username);
        userInfos.put("userEncrypt", userEncrypt);
        String userPost = HttpUtils.post("http://localhost:18088/us-admin/remote/remoteauth", userInfos);
        Map<String ,Object> r = gson.fromJson(userPost, map.getClass());
        Boolean auth = (Boolean) r.get("auth");
        if (auth) {
            String token = tokenGenerator.generate(username, password);
            response.setHeader("Authorization", token);
            redisUtil.opsForValue().set(token, username);
        }
        if (auth) {
            return "/PassInstant/passInstant/passInstant.html";
        } else {
            return "login.html";
        }
    }

    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String getLoginPage(@RequestHeader(value = "Authorization", required = false) String token) {
        if (!StringUtils.isNullOrEmpty(token)) {
            if (redisUtil.opsForValue().get(token) != null) {
                return "/PassInstant/passInstant/passInstant.html";
            }
        }
        return "/login.html";
    }

}
