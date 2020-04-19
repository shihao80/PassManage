package com.passuser.net.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisTokenUtil {

    @Autowired
    Md5TokenGenerator tokenGenerator;

    @Autowired
    private static RedisTemplate<String,String> redisUtil;


    public String returnToken(String username,String password){
        String token = tokenGenerator.generate(username, password);
        redisUtil.opsForValue().set(username, token);
        //设置key生存时间，当key过期时，它会被自动删除，时间是秒
        redisUtil.expire(username, ConstantKit.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
        redisUtil.opsForValue().set(token, username);
        redisUtil.expire(token, ConstantKit.TOKEN_EXPIRE_TIME,TimeUnit.MINUTES);
        Long currentTime = System.currentTimeMillis();
        redisUtil.opsForValue().set(token + username, currentTime.toString());
        //用完关闭
        return token;
    }
}
