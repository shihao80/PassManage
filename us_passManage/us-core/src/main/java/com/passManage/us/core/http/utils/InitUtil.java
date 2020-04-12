package com.passManage.us.core.http.utils;

import com.passManage.us.core.http.dto.ReqHeader;
import com.passManage.us.core.http.dto.ReqReturn;
import com.passManage.us.core.utils.StringUtil;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class InitUtil {
    private static volatile OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient(){
        if(okHttpClient == null){
            synchronized (InitUtil.class){
                if(okHttpClient == null){
                    okHttpClient = getUnsafeOkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 获取OkHttpClient
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        long timeOut = 30;
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.connectTimeout(timeOut, TimeUnit.SECONDS).readTimeout(timeOut,TimeUnit.SECONDS).writeTimeout(timeOut,TimeUnit.SECONDS).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static ReqHeader initReqHeader(ReqHeader reqHeader){
        if(reqHeader==null){
            reqHeader=new ReqHeader();
        }
        if(reqHeader.getTimeOut()==null){
            reqHeader.setTimeOut(5000);
        }
        if(StringUtil.isBlank(reqHeader.getEncode())){
            reqHeader.setEncode("UTF-8");
        }
        if(reqHeader.getHeaderMap()==null){
            reqHeader.setHeaderMap(new HashMap<String, String>());
        }
        return reqHeader;
    }

    public static boolean success200(ReqReturn reqReturn){
        return "200".equals(reqReturn.getStatus());
    }


    public static Map<String, String[]> coverMap(Map<String, String> simpleQueryMap){
        Map<String, String[]> queryMap = new HashMap<>();
        if(simpleQueryMap!=null && !simpleQueryMap.isEmpty()){
            Set<String> keys = simpleQueryMap.keySet();
            for(String key:keys){
                queryMap.put(key,new String[]{simpleQueryMap.get(key)});
            }
        }
        return queryMap;
    }
    public static Map<String, String[]> coverObjMap(Map<String, Object> simpleQueryMap){
        Map<String, String[]> queryMap = new HashMap<>();
        if(simpleQueryMap!=null && !simpleQueryMap.isEmpty()){
            Set<String> keys = simpleQueryMap.keySet();
            for(String key:keys){
                if(simpleQueryMap.get(key)!=null){
                    queryMap.put(key,new String[]{simpleQueryMap.get(key).toString()});
                }
            }
        }
        return queryMap;
    }
}
