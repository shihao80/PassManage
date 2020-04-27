package com.passManage.us.admin.common.util;

import com.google.gson.Gson;

import com.passManage.us.admin.common.KeyUtils.Sm4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResolveResponUtils {

    @Autowired
    private  RedisTemplate<String,String> redisTemplate;

    @Autowired
    private  Map<String ,Object> map = new HashMap<>();

    public  Map<String ,Object> resolveGetRespons(String url, Map<String, String> paramsMap){
        String getRanStr = HttpUtils.get(url, paramsMap);
        return getResponse(getRanStr);
    }

    public  Map<String ,Object> resolvePostRespons(String url, Map<String, String> paramsMap){
        String getRanStr = HttpUtils.post(url, paramsMap);
        return getResponse(getRanStr);
    }

    public  Map<String ,Object> resolvePostJsonRespons(String url, String postJson){
        String getRanStr = HttpUtils.HttpPostWithJson(url, postJson);
        return getResponse(getRanStr);
    }

    public  String getGetResponseData(String url,Map<String, String> paramsMap, String dataKey) throws Exception {
        String getRanStr = HttpUtils.get(url, paramsMap);
        return dealGetResponseData(dataKey, getRanStr);
    }

    public  String getPostJsonResponseData(String url,String postJson, String dataKey) throws Exception {
        String getRanStr = HttpUtils.HttpPostWithJson(url, postJson);
        return dealGetResponseData(dataKey, getRanStr);
    }

    public  String getPostResponseData(String url,Map<String, String> paramsMap, String dataKey) throws Exception {
        String getRanStr = HttpUtils.post(url, paramsMap);
        return dealGetResponseData(dataKey, getRanStr);
    }

    public  Map<String ,String> getGetResponseDecryptData(String url,Map<String, String> paramsMap, List<String> dataKey) throws Exception {
        String getRanStr = HttpUtils.get(url, paramsMap);
        return dealResponseDecryptData(getRanStr,dataKey);
    }

    public  Map<String ,String> getPostResponseDecryptData(String url,Map<String, String> paramsMap, List<String> dataKey) throws Exception {
        String getRanStr = HttpUtils.post(url, paramsMap);
        return dealResponseDecryptData( getRanStr,dataKey);
    }

    public  Map<String ,String> getPostJsonResponseDecryptData(String url, String postjson, List<String> dataKey) throws Exception {
        String getRanStr = HttpUtils.HttpPostWithJson(url, postjson);
        return dealResponseDecryptData( getRanStr,dataKey);
    }

    private  Map<String ,String> dealResponseDecryptData(String getRanStr, List<String> dataKey) throws Exception {
        String sessionkey = redisTemplate.opsForValue().get("sessionkey");
        Map<String ,Object> response = getResponse(getRanStr);
        Map<String ,String> perDatas = new HashMap<>();
        if(response!= null){
            for (String key : dataKey) {
                perDatas.put(key, Sm4Util.decryptEcb(sessionkey, response.get(key).toString()));
            }
        }else{
            throw new Exception("Response is not defined");
        }
        return perDatas;
    }

    private  String dealResponseListDecryptData(String dataKey, String getRanStr) throws Exception {
        String sessionkey = redisTemplate.opsForValue().get("sessionkey");
        Map<String ,Object> response = getResponse(getRanStr);
        String perData = "";
        if(response!= null){
            perData = Sm4Util.decryptEcb(sessionkey, response.get(dataKey).toString());
        }else{
            throw new Exception("Response is not defined");
        }
        return perData;
    }


    private  String dealGetResponseData(String dataKey, String getRanStr) throws Exception {
        Map<String ,Object> response = getResponse(getRanStr);
        String perData = "";
        if (response != null) {
            perData = response.get(dataKey).toString();
        } else {
            throw new Exception("Response is not defined");
        }
        return perData;
    }

    private  Map<String ,Object> getResponse(String getRanStr) {
        Gson gson = new Gson();
        return gson.fromJson(getRanStr, map.getClass());
    }

}
