package com.passManage.us.web.common.config;

import com.passManage.us.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置
 * 集群模式 支持1到多个redis服务
 */

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;


    //默认不开启集群模式 因为集群模式部署比较麻烦 但是支持  注释打开后 让RedisUtil继承 RedisClusterDefinedUtil
/*    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.cluster.connectionTimeout}")
    private int connectionTimeout;

    @Value("${spring.redis.cluster.soTimeout}")
    private int soTimeout;

    @Value("${spring.redis.cluster.maxAttempts}")
    private int maxAttempts;


    @Bean
     public JedisCluster jedisCluster(){
        Set<HostAndPort> jedisClusterNode = null;
        if(StringUtil.isNotBlank(clusterNodes)){
            jedisClusterNode = new HashSet<>();
            String[] arr = clusterNodes.split(",");
            for(String hostAndPot : arr){
                String[] hp = hostAndPot.split(":");
                jedisClusterNode.add(new HostAndPort(hp[0].trim(),Integer.valueOf(hp[1].trim())));
            }
        }else {
            return null;
        }
        if(StringUtil.isBlank(password)){
            return new JedisCluster(jedisClusterNode,connectionTimeout,soTimeout,maxAttempts,new GenericObjectPoolConfig());
        }else {
            return new JedisCluster(jedisClusterNode,connectionTimeout,soTimeout,maxAttempts,password,new GenericObjectPoolConfig());
        }
    }*/

//单机模式
    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setMinIdle(0);
        JedisPool jedisPool;

        if(StringUtil.isNotBlank(password)){
            jedisPool = new JedisPool(jedisPoolConfig, host,port, 10000, password);
        }else {
            jedisPool = new JedisPool(jedisPoolConfig, host,port, 10000);
        }
        return jedisPool;
    }

}
