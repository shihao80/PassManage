package com.passuser.net.controller.user;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import com.passuser.net.KeyUtils.Sm4Util;
import com.passuser.net.annotation.AuthToken;
import com.passuser.net.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class UserController {

    @Resource
    Md5TokenGenerator tokenGenerator;

    @Autowired
    private ResolveResponUtils resolveResponUtils;

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

    @RequestMapping("/user_userAdd")
    public String getUserRegister(){
        return "/system/user/user_teacherAdd.html";
    }

    @RequestMapping("/changePassword")
    public String changPassword(){
        return "/system/user/user_changPwd";
    }

//    /**
//     * 添加用户
//     */
//    @RequestMapping("/addUser")
//    public String addUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
//
//        this.userService.insert(UserFactory.createUser(user));
//        return SUCCESS_TIP;
//    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/changPassword",method = RequestMethod.POST)
    @AuthToken
    public String addUser(@RequestParam("oldpassword") String oldpassword, @RequestParam("firpassword") String firpassword, @RequestParam("secpassword") String secpassword, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>();
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String username = redisTemplate.opsForValue().get(token);
        if (!firpassword.equals(secpassword)) {
            return "404.html";
        }
        Gson gson = new Gson();
        hashMap.put("oldpassword", oldpassword);
        hashMap.put("newpassword", firpassword);
        hashMap.put("username", username);
        String hashMapJson = gson.toJson(hashMap);
        String sessionkey = redisTemplate.opsForValue().get("sessionkey");
        String userEncrypt = Sm4Util.encryptEcb(sessionkey, hashMapJson);
        R r = R.ok().put("encryptData", userEncrypt);
        String postJson = gson.toJson(r);
        String flag = resolveResponUtils.getPostJsonResponseData("http://localhost:18088/us-admin/remote/changpwd/" + username, postJson, "flag");
        return flag.equals("true")?"redirect:/passInstant":"404.html";
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public String register(@RequestParam("username")String username,@RequestParam("password")String password) throws Exception {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("password",password);
        String flag = resolveResponUtils.getPostResponseData("http://localhost:18088/us-admin/remote/register/", hashMap, "flag");
        return flag.equals("true")?"redirect:/login":"404.html";
    }
}
