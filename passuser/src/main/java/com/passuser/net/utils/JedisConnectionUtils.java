package com.passuser.net.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class JedisConnectionUtils {

    private static Jedis jedis = new Jedis("127.0.0.1", 6379);

    public static Jedis getJedis() {
        return jedis;
    }
}
