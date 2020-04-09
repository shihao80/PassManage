package com.passManage.us.core.http.dto;

import okhttp3.Headers;

import java.io.Serializable;

/**
 * Created by hzhedongyu on 2015/10/14.
 */
public class ReqReturn implements Serializable{
    private boolean success;
    private String status;
    private String body;
    private Headers headers;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
