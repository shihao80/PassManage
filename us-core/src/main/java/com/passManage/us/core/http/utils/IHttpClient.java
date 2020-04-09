package com.passManage.us.core.http.utils;


import com.passManage.us.core.http.callback.OkNoSyncCallback;
import com.passManage.us.core.http.dto.ReqHeader;
import com.passManage.us.core.http.dto.ReqReturn;

import java.io.File;
import java.util.Map;

/**
 * Created by hzhedongyu on 2015/10/14.
 */
public interface IHttpClient {
    //同步请求
    ReqReturn requestByJson(String reqUrl, String jsonReq);
    ReqReturn requestByJson(String reqUrl, String jsonReq, ReqHeader reqHeader);
    ReqReturn request(String reqUrl);
    ReqReturn request(String reqUrl, Map<String, String[]> reqMap);
    ReqReturn request(String reqUrl, Map<String, String[]> reqMap, ReqHeader reqHeader);
    ReqReturn request(String reqUrl, Map<String, String[]> reqMap, Map<String, File> fileMap, ReqHeader reqHeader);

    //异步请求
    void noSyncRequestByJson(String reqUrl, String jsonReq, OkNoSyncCallback callback);
    void noSyncRequestByJson(String reqUrl, String jsonReq, ReqHeader reqHeader, OkNoSyncCallback callback);
    void noSyncRequest(String reqUrl, OkNoSyncCallback callback);
    void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, OkNoSyncCallback callback);
    void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, ReqHeader reqHeader, OkNoSyncCallback callback);
    void noSyncRequest(String reqUrl, Map<String, String[]> reqMap, Map<String, File> fileMap, ReqHeader reqHeader, OkNoSyncCallback callback);

    //异步请求
    void noSyncSimpleRequest(String reqUrl, Map<String, String> reqMap, OkNoSyncCallback callback);
    void noSyncSimpleRequest(String reqUrl, Map<String, String> reqMap, ReqHeader reqHeader, OkNoSyncCallback callback);
    void noSyncSimpleRequest(String reqUrl, Map<String, String> reqMap, Map<String, File> fileMap, ReqHeader reqHeader, OkNoSyncCallback callback);
}
