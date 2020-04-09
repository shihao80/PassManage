package com.passManage.us.core.http.utils;

import com.passManage.us.core.http.callback.OkNoSyncCallback;
import com.passManage.us.core.http.dto.ReqHeader;
import com.passManage.us.core.http.dto.ReqReturn;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * multipart post 有文件上传的表单
 * Created by hzhedongyu on 2015/10/14.
 */
public class HttpClientMultipartPost implements IHttpClient {

    private static final HttpClientMultipartPost instance = new HttpClientMultipartPost();
    private HttpClientMultipartPost(){

    }

    public static HttpClientMultipartPost get(){
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    /*    Map<String,File> fileMap = new HashMap<String, File>();
        fileMap.put("myfiles",new File("D:\\myfile\\signcheck-0.0.7.jar"));
        ReqReturn reqReturn =HttpClientMultipartPost.get().request("http://localhost/tianyu/center/fileupload/business", null, fileMap, null);
        System.out.println(reqReturn.getBody());*/
    }

    @Override
    public ReqReturn requestByJson(String reqUrl, String jsonReq) {
        return null;
    }

    @Override
    public ReqReturn requestByJson(String reqUrl, String jsonReq, ReqHeader reqHeader) {
        return null;
    }

    @Override
    public ReqReturn request(String reqUrl) {
        return null;
    }

    @Override
    public ReqReturn request(String reqUrl, Map<String, String[]> reqMap) {
        return null;
    }

    @Override
    public ReqReturn request(String reqUrl, Map<String, String[]> reqMap, ReqHeader reqHeader) {
        return null;
    }

    @Override
    public ReqReturn request(String reqUrl, Map<String, String[]> reqMap, Map<String, File> fileMap, ReqHeader reqHeader) {

        return null;
    }

    @Override
    public void noSyncRequestByJson(String reqUrl, String jsonReq, OkNoSyncCallback callback) {

    }

    @Override
    public void noSyncRequestByJson(String reqUrl, String jsonReq, ReqHeader reqHeader, OkNoSyncCallback callback) {

    }

    @Override
    public void noSyncRequest(String reqUrl, OkNoSyncCallback callback) {

    }

    @Override
    public void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, OkNoSyncCallback callback) {

    }

    @Override
    public void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, ReqHeader reqHeader, OkNoSyncCallback callback) {

    }

    @Override
    public void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, Map<String, File> fileMap, ReqHeader reqHeader, OkNoSyncCallback callback) {

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
