package com.passuser.net.utils;

import com.google.gson.Gson;
import com.passuser.net.KeyUtils.Sm4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolveResponUtils {

    @Autowired
    private static RedisTemplate<String,String> redisUtil;

    @Autowired
    private static Map<String ,Object> map = new HashMap<>();

    public static Map<String ,Object> resolveGetRespons(String url, Map<String, String> paramsMap){
        String getRanStr = HttpUtils.get(url, paramsMap);
        return getResponse(getRanStr);
    }

    public static Map<String ,Object> resolvePostRespons(String url, Map<String, String> paramsMap){
        String getRanStr = HttpUtils.post(url, paramsMap);
        return getResponse(getRanStr);
    }

    public static Map<String ,Object> resolvePostJsonRespons(String url, String postJson){
        String getRanStr = HttpUtils.HttpPostWithJson(url, postJson);
        return getResponse(getRanStr);
    }

    public static String getGetResponseData(String url,Map<String, String> paramsMap, String dataKey) throws Exception {
        String getRanStr = HttpUtils.get(url, paramsMap);
        return dealGetResponseData(dataKey, getRanStr);
    }

    public static String getPostJsonResponseData(String url,String postJson, String dataKey) throws Exception {
        String getRanStr = HttpUtils.HttpPostWithJson(url, postJson);
        return dealGetResponseData(dataKey, getRanStr);
    }

    public static String getPostResponseData(String url,Map<String, String> paramsMap, String dataKey) throws Exception {
        String getRanStr = HttpUtils.post(url, paramsMap);
        return dealGetResponseData(dataKey, getRanStr);
    }

    public static Map<String ,String> getGetResponseDecryptData(String url,Map<String, String> paramsMap, List<String> dataKey) throws Exception {
        String getRanStr = HttpUtils.get(url, paramsMap);
        return dealResponseDecryptData(getRanStr,dataKey);
    }

    public static Map<String ,String> getPostResponseDecryptData(String url,Map<String, String> paramsMap, List<String> dataKey) throws Exception {
        String getRanStr = HttpUtils.post(url, paramsMap);
        return dealResponseDecryptData( getRanStr,dataKey);
    }

    public static Map<String ,String> getPostJsonResponseDecryptData(String url, String postjson, List<String> dataKey) throws Exception {
        String getRanStr = HttpUtils.HttpPostWithJson(url, postjson);
        return dealResponseDecryptData( getRanStr,dataKey);
    }

    private static Map<String ,String> dealResponseDecryptData(String getRanStr, List<String> dataKey) throws Exception {
        String sessionkey = redisUtil.opsForValue().get("sessionkey");
        Map<String ,Object> response = getResponse(getRanStr);
        Map<String ,String> perDatas = new HashMap<>();
        if(response!= null){
            for (String key : dataKey) {
                perDatas.put(key,Sm4Util.decryptEcb(sessionkey, response.get(key).toString()));
            }
        }else{
            throw new Exception("Response is not defined");
        }
        return perDatas;
    }

    private static String dealResponseListDecryptData(String dataKey, String getRanStr) throws Exception {
        String sessionkey = redisUtil.opsForValue().get("sessionkey");
        Map<String ,Object> response = getResponse(getRanStr);
        String perData = "";
        if(response!= null){
            perData = Sm4Util.decryptEcb(sessionkey, response.get(dataKey).toString());
        }else{
            throw new Exception("Response is not defined");
        }
        return perData;
    }


    private static String dealGetResponseData(String dataKey, String getRanStr) throws Exception {
        Map<String ,Object> response = getResponse(getRanStr);
        String perData = "";
        if (response != null) {
            perData = response.get(dataKey).toString();
        } else {
            throw new Exception("Response is not defined");
        }
        return perData;
    }

    private static Map<String ,Object> getResponse(String getRanStr) {
        Gson gson = new Gson();
        return gson.fromJson(getRanStr, map.getClass());
    }

}
