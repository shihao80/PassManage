package com.passuser.net.controller.user;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import com.passuser.net.KeyUtils.Sm4Util;
import com.passuser.net.utils.CookieUtils;
import com.passuser.net.utils.HttpUtils;
import com.passuser.net.utils.Md5TokenGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Resource
    Md5TokenGenerator tokenGenerator;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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
        redisTemplate.opsForValue().set("sessionkey", sessionkey);
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
            CookieUtils.setCookie(request,response,"Authorization", token);
            response.setHeader("Authorization", token);
            redisTemplate.opsForValue().set(token, username);
            redisTemplate.opsForValue().set(token+username,System.currentTimeMillis()+"");
        }
        if (auth) {
            return "/PassInstant/passInstant/passInstant.html";
        } else {
            return "login.html";
        }
    }

    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String getLoginPage(HttpServletRequest request,HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        if (!StringUtils.isNullOrEmpty(token)) {
            if (redisTemplate.opsForValue().get(token) != null) {
                return "/PassInstant/passInstant/passInstant.html";
            }
        }
        return "/login.html";
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        CookieUtils.deleteCookie(request,response,"Authorization");
        Enumeration em = request.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            request.getSession().removeAttribute(em.nextElement().toString());
        }
        return "login.html";
    }

}
