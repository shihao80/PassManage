package com.passManage.us.core.http.dto;

import java.util.Map;

/**
 * Created by hzhedongyu on 2015/10/14.
 */
public class ReqHeader {
    private Integer timeOut;

    private String encode;

    private Map<String,String> headerMap;

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }
}
