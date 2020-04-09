package com.passManage.us.admin.common.interceptor.limit;

import com.passManage.us.core.cache.redis.RedisUtil;
import com.passManage.us.core.common.exception.BusinessException;
import com.passManage.us.core.serialize.Restrict;
import com.passManage.us.core.utils.IpUtil;
import com.passManage.us.core.utils.JsonUtil;
import com.passManage.us.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * author: www.magicalcoder.com
 * date:2018/5/14
 * function:
 * 访问限制 配置在数据库中 code=REQ_RATE name为配置内容
 */
@Slf4j
@Component
public class RequestRateLimiter {

    private String reqRateCode = "REQ_RATE";
    private String redisPrefix = "request_rate_";
    //@Resource
    //private DictService dictService;
    @Resource
    private RedisUtil redisUtil;

    public void aquire(HttpServletRequest request){
        String json = "";//dictService.getDictNameCache(reqRateCode);
        if(StringUtil.isNotBlank(json)){
            Restrict restrict = JsonUtil.getObjectFromJSONString(json,Restrict.class);
            if(restrict!=null){
                //
                String path = request.getServletPath();
                if(!path.matches(restrict.getServletPathReg())){
                    return;
                }
                String strategy = restrict.getStrategy();
                if(StringUtil.isBlank(strategy) || "no".equals(strategy)){
                    return;
                }
                if("ip".equals(strategy)){
                    ipCheck(path,restrict,request);
                }else if("user".equals(strategy)){
                    userCheck(path,restrict,request);
                }else if("ip_user".equals(strategy)){
                    ipCheck(path,restrict,request);
                    userCheck(path,restrict,request);
                }
            }
        }
    }

    private void ipCheck(String path,Restrict restrict, HttpServletRequest request){
        String ip = IpUtil.getIp(request);
        if(!redisUtil.tryIncr(redisPrefix+ip,restrict.getIpExpireSeconds(),restrict.getIpMaxReqTimes())){
            log.info(String.format("ip=[%s]请求[%s]过于频繁，已超过系统拒绝请求边界每[%s]秒请求[%s]次",ip,path,restrict.getIpExpireSeconds(),restrict.getIpMaxReqTimes()));
//            throw new BusinessException(CommonReturnCode.REQUEST_IP_ATTACK);
        }
    }
    private void userCheck(String path,Restrict restrict, HttpServletRequest request){
        Long uid = null;
        try {
//            uid = UserInfoUtil.getUid(request);
        }catch (BusinessException e){

        }
        if(uid==null){
            return;
        }
        if(!redisUtil.tryIncr(redisPrefix+uid,restrict.getUserExpireSeconds(),restrict.getUserMaxReqTimes())){
            log.info(String.format("uid=[%s]请求[%s]过于频繁，已超过系统拒绝请求边界每[%s]秒请求[%s]次",uid,path,restrict.getUserExpireSeconds(),restrict.getUserMaxReqTimes()));
//            throw new BusinessException(CommonReturnCode.REQUEST_IP_ATTACK);
        }
    }

}
