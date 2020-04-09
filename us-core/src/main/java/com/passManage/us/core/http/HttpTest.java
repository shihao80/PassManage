package com.passManage.us.core.http;

import java.util.HashMap;
import java.util.Map;

/**
 * author: hedy
 * date:2018/4/27
 * function:
 */
public class HttpTest {
    public static void main(String[] args) {
        Map<String,String[]> reqMap = new HashMap<>();
//        reqMap.put("hi",new String[]{"1","2"});
        reqMap.put("haha",new String[]{"1"});
        String reqUrl = "http://www?d=1&d=3&";
//        System.out.println(getUrl(reqUrl,reqMap));
    }


}
