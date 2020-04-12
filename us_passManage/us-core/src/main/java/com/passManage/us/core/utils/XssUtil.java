package com.passManage.us.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class XssUtil {
    private static String from = "src=\"//";
    private static String to = "src=\"http://";

    /**
     * 过滤富文本中可能包含的脚本攻击
     * @return
     */
    public static String filterFWBXssScript(String mayBeContainXssHtml){
        if(StringUtil.isBlank(mayBeContainXssHtml)){
            return mayBeContainXssHtml;
        }
        //图片上传的地址是//开头 不支持指定协议，jsoup要求必须指定协议
        mayBeContainXssHtml = mayBeContainXssHtml.replaceAll(from,to);
        Whitelist whitelist = Whitelist.relaxed().addTags("img").addAttributes("img", "align", "alt", "height", "src", "title", "width","style").addProtocols("img","src","http","https");

        String cleanHtml = Jsoup.clean(mayBeContainXssHtml,whitelist);
        return cleanHtml.replaceAll(to,from);
    }
}
