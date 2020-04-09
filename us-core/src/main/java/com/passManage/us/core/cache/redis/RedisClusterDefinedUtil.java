package com.passManage.us.core.cache.redis;

import com.passManage.us.core.protobuf.ProtostuffUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 集群模式下的redis 默认是不开启的
 *
 * @author dyhe2
 * @date 2018-8-28
 * 集群单机都支持 过度完将删除RedisDefinedUtil 统一使用集群模式
 */
//@Component
public class RedisClusterDefinedUtil {

//    @Autowired
    private JedisCluster jedisCluster;

    //包装key
    private String wrapKey(String keyPrefix, String key) {
        return new StringBuilder(keyPrefix).append(key).toString();
    }

    /**
     * 设置key-value对
     *
     * @param key
     * @param value
     */
    public boolean set(String keyPrefix, String key, String value) {
        jedisCluster.set(wrapKey(keyPrefix, key), value);
        return true;
    }


    /**
     * 设置key-value对，并设置过期时间（单位：s）
     *
     * @param key
     * @param value
     * @param seconds
     */
    public boolean setEx(String keyPrefix, String key, String value, int seconds) {
        jedisCluster.setex(wrapKey(keyPrefix, key), seconds, value);
        return true;
    }

    /**
     * 根据key查询value
     *
     * @param key
     * @return
     */
    public String get(String keyPrefix, String key) {
        return jedisCluster.get(wrapKey(keyPrefix, key));
    }

    /**
     * 删除key-value对
     *
     * @param key
     */
    public void del(String keyPrefix, String key) {
        jedisCluster.del(wrapKey(keyPrefix, key));
    }

    /**
     * 对key对应的值减一
     *
     * @param key
     * @return
     */
    public long decr(String keyPrefix, String key) {
        return jedisCluster.decr(wrapKey(keyPrefix, key));
    }

    /**
     * 对key对应的值减num
     *
     * @param key
     * @return
     */
    public long decrBy(String keyPrefix, String key, long num) {
        return jedisCluster.decrBy(wrapKey(keyPrefix, key), num);
    }

    /**
     * 对key对应的值加num
     *
     * @param key
     * @return
     */
    public long incrBy(String keyPrefix, String key, long num) {
        return jedisCluster.incrBy(wrapKey(keyPrefix, key), num);
    }

    /**
     * @param key
     * @return Map<String , String>
     * @throws
     * @Title: hGetAll
     * @Description: 获取redis hash结构对应key的值
     */
    public Map<String, String> hgetAll(String keyPrefix, String key) {
        return jedisCluster.hgetAll(wrapKey(keyPrefix, key));
    }

    /**
     * @param key
     * @param field
     * @param value
     * @return
     * @throws
     * @Title: hSet
     * @Description: 存入redis hash结构
     */
    public void hset(String keyPrefix, String key, String field, String value) {
        jedisCluster.hset(wrapKey(keyPrefix, key), field, value);
    }

    /**
     * @param key
     * @param field
     * @return String value
     * @throws
     * @Title: hGet
     * @Description: 获取对应hash下key的数据
     */
    public String hget(String keyPrefix, String key, String field) {
        String value = jedisCluster.hget(wrapKey(keyPrefix, key), field);
        return value;
    }

    /**
     * @param key
     * @param fields
     * @return
     * @throws
     * @Title: hDel
     * @Description: redis hash结构删除key下对应field的值
     */
    public void hdel(String keyPrefix, String key, String... fields) {
        key = wrapKey(keyPrefix, key);
        if (fields.length == 0) {
            jedisCluster.hdel(key);
        } else {
            for (String field : fields) {
                jedisCluster.hdel(key, field);
            }
        }
    }

    /**
     * @param key
     * @param field
     * @return boolean
     * @throws
     * @Title: HExists
     * @Description: redis hash结构：查看哈希表 key 中，给定域 field 是否存在。
     */
    public boolean hexists(String keyPrefix, String key, String field) {
        return jedisCluster.hexists(wrapKey(keyPrefix, key), field);
    }

    /**
     * @param key
     * @return boolean
     * @throws
     * @Title: HExists
     * @Description: redis hash结构：获取所有hash表的field
     */
    public Set<String> hkeys(String keyPrefix, String key) {
        return jedisCluster.hkeys(wrapKey(keyPrefix, key));
    }

    public Long hincrby(String keyPrefix, String key, String field, Long count) {
        return jedisCluster.hincrBy(wrapKey(keyPrefix, key), field, count);
    }

    /**
     * Set集合添加数据
     *
     * @param key
     * @param val
     */
    public void sadd(String keyPrefix, String key, String val) {
        jedisCluster.sadd(wrapKey(keyPrefix, key), val);
    }

    /**
     * 设置
     *
     * @param key
     * @param val
     * @return
     */
    public Long setnx(String keyPrefix, String key, String val) {
        try {
            return jedisCluster.setnx(wrapKey(keyPrefix, key), val);
        } catch (Exception e) {
            return 0l;
        }
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param sec
     */
    public void expire(String keyPrefix, String key, int sec) {
        jedisCluster.expire(wrapKey(keyPrefix, key), sec);
    }

    /**
     * 把java对象存入redis
     *
     * @param key
     * @param obj
     * @param expireSecond 过期时间
     * @param <T>
     * @author hedy
     */
    public <T> void setexObj(String keyPrefix, String key, int expireSecond, T obj) {
        key = wrapKey(keyPrefix, key);
        try {
            if (obj == null) {
                jedisCluster.del(key);
                return;
            }
            byte[] bytes = ProtostuffUtil.serialize(obj);//序列化
            jedisCluster.setex(key.getBytes("UTF-8"), expireSecond, bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从redis获取java对象
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getObj(String keyPrefix, String key, Class<T> tClass) {
        try {
            byte[] bytes = jedisCluster.get(wrapKey(keyPrefix, key).getBytes("UTF-8"));
            if (bytes != null) {
                return ProtostuffUtil.deserialize(bytes);//序列化
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public boolean exist(String keyPrefix, String key) {
        return jedisCluster.exists(wrapKey(keyPrefix, key));
    }

    /**
     * redis简单分布式锁 最主要是一个命令执行
     *
     * @param key
     * @param uuid      UUID.randomUUID().toString() 解锁使用
     * @param milliTime 毫秒
     * @return
     */
    public boolean tryDistributeLock(String keyPrefix, String key, String uuid, int milliTime) {
        String result = jedisCluster.set(wrapKey(keyPrefix, key), uuid, "NX", "PX", milliTime);
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param key
     * @param uuid
     * @return
     */
    public void releaseDistributeLock(String keyPrefix, String key, String uuid) {
        String redisScript = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end";
        jedisCluster.eval(redisScript, Collections.singletonList(wrapKey(keyPrefix, key)), Collections.singletonList(uuid));
    }

    /**
     * 获取指定键对应所有值的集合
     *
     * @param key
     * @return
     */
    public Set getValSet(String keyPrefix, String key) {
        Set<String> valueSet = jedisCluster.smembers(wrapKey(keyPrefix, key));
        return valueSet;
    }

    /**
     * 获取指定键及对应条数值的集合
     *
     * @param key
     * @param count
     * @return
     */
    public List<String> srandmber(String keyPrefix, String key, int count) {
        List<String> valueSet = jedisCluster.srandmember(wrapKey(keyPrefix, key), count);
        return valueSet;
    }


    /**
     * 移除集合中的成员
     *
     * @param key
     * @param value
     * @return
     */
    public void removeMem(String keyPrefix, String key, String value) {
        jedisCluster.srem(wrapKey(keyPrefix, key), value);
    }

    /**
     * 在制定expireSeconds时间内 key自增的最大
     * redis 执行lua脚本竟然不支持 >= <= 所以要改成> 或 == 或< 分开判断
     *
     * @param key
     * @param expireSeconds
     * @param maxTime
     * @return
     */
    public boolean tryIncr(String keyPrefix, String key, int expireSeconds, int maxTime) {
        StringBuilder redisScript = new StringBuilder("local current;");
        redisScript.append(" current = redis.call('incr',KEYS[1]); ");
        redisScript.append("  local curNo = tonumber(current);");
        redisScript.append("  local maxTime = tonumber(ARGV[2]);");
        redisScript.append(" if curNo > 1 then ");
        redisScript.append(" 	if curNo < maxTime then ");
        redisScript.append("     	return 2; ");
        redisScript.append(" 	elseif curNo == maxTime then ");
        redisScript.append("     	return 3; ");
        redisScript.append("	else ");
        redisScript.append("		return -1; ");//超标
        redisScript.append("   end ");
        redisScript.append(" else");
        redisScript.append(" 	redis.call('expire',KEYS[1],ARGV[1]); ");
        redisScript.append("     return 1; ");
        redisScript.append(" end ");
        List<String> args = new ArrayList<>(2);
        args.add(String.valueOf(expireSeconds));
        args.add(String.valueOf(maxTime));
        Object ret = jedisCluster.eval(redisScript.toString(), Collections.singletonList(wrapKey(keyPrefix, key)), args);
        Integer value = Integer.valueOf(ret.toString());
        if (value < 0) {
            return false;
        }
        return true;
    }
}
