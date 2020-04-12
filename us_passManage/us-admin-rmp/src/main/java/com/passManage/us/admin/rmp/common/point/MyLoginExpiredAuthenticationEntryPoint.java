package com.passManage.us.admin.rmp.common.point;

import com.passManage.us.admin.rmp.common.SecurityReturnCode;
import com.passManage.us.core.serialize.JsonOutWriter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * author:www.magicalcoder.com
 * date:2018/7/17
 * function: 自定义一下 当用户登陆超时出错 框架捕获了匿名用户访问接口后 调用这里 提示用户重新登录即可
 */
public class MyLoginExpiredAuthenticationEntryPoint extends JsonOutWriter implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String servletPath = request.getServletPath();
        if(servletPath.startsWith("/admin/page/") || servletPath.startsWith("/admin/page_v2/")){//返回到登录超时页面 这种格式的请求都是列表页 详情页跳转 所以应该展示到登录超时页面
            String path = request.getContextPath();
            String CTX = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";//这个是登录页
            String redirectUrl = URLEncoder.encode(CTX+servletPath+"?"+request.getQueryString());
            response.sendRedirect(CTX+"?from=innerLogin&redirectUrl="+redirectUrl);//参考login.js处理逻辑 redirectUrl记住之前地址
        }else if(servletPath.startsWith("/admin/rmp/index")){
            String path = request.getContextPath();
            String CTX = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";//这个是登录页
            response.sendRedirect(CTX+"?from=outerLogin");//参考login.js处理逻辑
        }
        else {
            toWebFail(response,SecurityReturnCode.RE_LOGIN);
        }
    }
}
