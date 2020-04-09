package com.passManage.us.admin.rmp.common.controller.web.user;

import com.passManage.us.admin.common.config.UploadFilePathConfig;
import com.passManage.us.admin.rmp.constant.PermissionConstant;
import com.passManage.us.core.cache.redis.RedisUtil;
import com.passManage.us.core.identifyingcode.CreateIdentifyingCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: www.magicalcoder.com
 * date:2018/7/10
 * function:
 */
@Controller
public class WebSecurityController {

    /*是否是发布状态的js*/
    @Value("${magicalcoder.publish}")
    private boolean publish;
    /*的前缀*/
    @Resource
    private UploadFilePathConfig uploadFilePathConfig;

    @RequestMapping(value = "/")
    public String index(HttpServletRequest request, ModelMap modelMap){
        initCtxMap(modelMap);
        String sessionId = request.getSession().getId();
        modelMap.addAttribute("sessionId",sessionId);
        return "login";
    }
    @Resource
    private RedisUtil redisUtil;


    private void initCtxMap(ModelMap modelMap){
        modelMap.put("publish",publish);
        modelMap.put("fileExtraAddPrefix",uploadFilePathConfig.getFileExtraAddPrefix());
        modelMap.put("requestPrefix",uploadFilePathConfig.getRequestPrefix());
    }

    //验证码
    @Resource
    private CreateIdentifyingCode createIdentifyingCode;
    @RequestMapping(value = "/web/code")
    public void code(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String random =createIdentifyingCode.create(response);
        String sessionId = request.getSession().getId();
        redisUtil.set(PermissionConstant.CODE_PREFIX+sessionId,random);
    }

}
