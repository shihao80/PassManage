package com.passuser.net.utils;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

public class RedisTokenUtil {

    @Autowired
    Md5TokenGenerator tokenGenerator;

    public String returnToken(String username,String password){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String token = tokenGenerator.generate(username, password);
        jedis.set(username, token);
        //设置key生存时间，当key过期时，它会被自动删除，时间是秒
        jedis.expire(username, ConstantKit.TOKEN_EXPIRE_TIME);
        jedis.set(token, username);
        jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);
        Long currentTime = System.currentTimeMillis();
        jedis.set(token + username, currentTime.toString());
        //用完关闭
        return token;
    }
}
