package com.passManage.us.core.http.utils;

import com.passManage.us.core.http.callback.OkNoSyncCallback;
import com.passManage.us.core.http.dto.ReqHeader;
import com.passManage.us.core.http.dto.ReqReturn;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

/**
 * Created by hzhedongyu on 2015/10/14.
 */
public class HttpClientGet implements IHttpClient {

    private static final HttpClientGet instance = new HttpClientGet();
    private HttpClientGet(){
    }

    public static void main(String[] args) {

    }

    public static HttpClientGet get(){
        return instance;
    }


    @Override
    public ReqReturn requestByJson(String reqUrl, String jsonReq) {
        throw new RuntimeException("get 不支持");
    }

    @Override
    public ReqReturn requestByJson(String reqUrl, String jsonReq, ReqHeader reqHeader) {
        throw new RuntimeException("get 不支持");
    }

    @Override
    public ReqReturn request(String reqUrl) {
        return request(reqUrl, null, null,null);
    }

    @Override
    public ReqReturn request(String reqUrl, Map<String, String[]> reqMap) {
        return request(reqUrl, reqMap, null,null);
    }

    @Override
    public ReqReturn request(String reqUrl, Map<String, String[]> reqMap, ReqHeader reqHeader) {
        return request(reqUrl,reqMap,null,reqHeader);
    }

    private static String getUrl(String reqUrl, Map<String, String[]> reqMap){
        StringBuffer dataURL = new StringBuffer(reqUrl);
        StringBuffer extraParam = new StringBuffer();
        String[] arr = reqUrl.split("\\?");
        if(arr.length==1){
            extraParam.append("?");
        }else if(arr.length == 2){
            if(!arr[1].endsWith("&")){
                extraParam.append("&");
            }
        }else {
            throw new RuntimeException("url格式不正确“?”分隔符超过2个");
        }

        if(reqMap!=null && !reqMap.isEmpty()){
            Set<String> keys = reqMap.keySet();
            for(String key:keys){
                String[] value = reqMap.get(key);
                for (String v:value){
                    extraParam.append(key).append("=").append(v).append("&");
                }
            }
        }
        if(extraParam.toString().endsWith("&")){
            extraParam.deleteCharAt(extraParam.length()-1);
        }
        return dataURL.append(extraParam).toString();
    }

    @Override
    public ReqReturn request(String reqUrl, Map<String, String[]> reqMap, Map<String, File> fileMap, ReqHeader reqHeader) {
        ReqReturn reqReturn = new ReqReturn();
        Request.Builder builder = new Request.Builder()
            .url(getUrl(reqUrl,reqMap));
        if(reqHeader!=null){
            Map<String,String> headerMap = reqHeader.getHeaderMap();
            if(headerMap!=null && !headerMap.isEmpty()){
                Set<String> keys = headerMap.keySet();
                for(String key:keys){
                    builder.addHeader(key,headerMap.get(key));
                }
            }
        }
        try {
            Call call = InitUtil.getOkHttpClient().newCall(builder.build());
            Response response = call.execute();//执行
            reqReturn.setStatus(response.code()+"");
            reqReturn.setSuccess(response.isSuccessful());
            reqReturn.setBody(response.body().string());
            reqReturn.setHeaders(response.headers());
        }catch (SocketTimeoutException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reqReturn;
    }

    @Override
    public void noSyncRequestByJson(String reqUrl, String jsonReq, OkNoSyncCallback callback) {
        throw new RuntimeException("get 不支持");
    }

    @Override
    public void noSyncRequestByJson(String reqUrl, String jsonReq, ReqHeader reqHeader, OkNoSyncCallback callback) {
        throw new RuntimeException("get 不支持");
    }

    @Override
    public void noSyncRequest(String reqUrl, OkNoSyncCallback callback) {
        noSyncRequest(reqUrl, null, null,null, callback);
    }

    @Override
    public void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, OkNoSyncCallback callback) {
        noSyncRequest(reqUrl, reqMap, null,null, callback);
    }

    @Override
    public void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, ReqHeader reqHeader, OkNoSyncCallback callback) {
        noSyncRequest(reqUrl, reqMap, null,reqHeader, callback);
    }

    @Override
    public void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, Map<String, File> fileMap, ReqHeader reqHeader, OkNoSyncCallback callback) {
        Request.Builder builder = new Request.Builder()
            .url(getUrl(reqUrl,reqMap));
        if(reqHeader!=null){
            Map<String,String> headerMap = reqHeader.getHeaderMap();
            if(headerMap!=null && !headerMap.isEmpty()){
                Set<String> keys = headerMap.keySet();
                for(String key:keys){
                    builder.addHeader(key,headerMap.get(key));
                }
            }
        }
        try {
            Call call = InitUtil.getOkHttpClient().newCall(builder.build());
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFailure();
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ReqReturn reqReturn = new ReqReturn();
                    reqReturn.setStatus(response.code()+"");
                    reqReturn.setSuccess(response.isSuccessful());
                    reqReturn.setBody(response.body().string());
                    reqReturn.setHeaders(response.headers());
                    Platform.runLater(() -> {//这里一般是更新主线程操作 必须用线程 否则会提示非主线程
                        callback.onResponse(reqReturn);
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void noSyncSimpleRequest(String reqUrl, Map<String, String> reqMap, OkNoSyncCallback callback) {
        noSyncRequest(reqUrl,InitUtil.coverMap(reqMap),callback);
    }

    @Override
    public void noSyncSimpleRequest(String reqUrl, Map<String, String> reqMap, ReqHeader reqHeader, OkNoSyncCallback callback) {
        noSyncRequest(reqUrl,InitUtil.coverMap(reqMap),reqHeader,callback);
    }

    @Override
    public void noSyncSimpleRequest(String reqUrl, Map<String, String> reqMap, Map<String, File> fileMap, ReqHeader reqHeader, OkNoSyncCallback callback) {
        noSyncRequest(reqUrl,InitUtil.coverMap(reqMap),fileMap,reqHeader,callback);
    }
}
