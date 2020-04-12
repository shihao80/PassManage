package com.passManage.us.admin.rmp.common.controller.admin;

import com.passManage.us.admin.common.config.UploadFilePathConfig;
import com.passManage.us.admin.rmp.dto.SysAdminUserDto;
import com.passManage.us.admin.rmp.dto.SysRoleDto;
import com.passManage.us.admin.rmp.util.AdminUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * author:www.magicalcoder.com
 * date:2018/7/31
 * function:
 */
@Controller
@RequestMapping(value = "/admin/")
public class AdminRmpController {
    /*是否是发布状态的js*/
    @Value("${magicalcoder.publish}")
    private boolean publish;
    /*的前缀*/
    @Resource
    private UploadFilePathConfig uploadFilePathConfig;

    @RequestMapping(value = "rmp/index")
    public String index(HttpServletRequest request,ModelMap modelMap){
        initCtxMap(modelMap);
        SysAdminUserDto adminUser = AdminUtil.getAdmin();
        SysRoleDto role = adminUser.getSysRoleDto();
        modelMap.addAttribute("categoryList",role.getSysModuleCategoryDtoList());
        return "index";
    }

    @RequestMapping(value = "rmp/main")
    public String main(ModelMap modelMap){
        initCtxMap(modelMap);
        return "main";
    }

    private void initCtxMap(ModelMap modelMap){
        modelMap.put("publish",publish);
        modelMap.put("fileExtraAddPrefix",uploadFilePathConfig.getFileExtraAddPrefix());
        modelMap.put("requestPrefix",uploadFilePathConfig.getRequestPrefix());
    }

    //映射所有界面 这里统一路径功能一做，几乎就实现了前后端分离了 不再允许java端拼接大量参数给界面
    @Deprecated
    @RequestMapping(value = "page/{tableName}/{page}")
    public String mapping(@PathVariable String tableName,@PathVariable String page,
                          ModelMap modelMap){
        initCtxMap(modelMap);
        String name = tableName.replaceAll("_","");
        return name +"/"+ name +"-"+ page;
    }

    //映射所有界面 这里统一路径功能一做，几乎就实现了前后端分离了 不再允许java端拼接大量参数给界面
    @RequestMapping(value = "page_v2/{tableName}/{page}")
    public String mappingV2(@PathVariable String tableName,@PathVariable String page,
                          ModelMap modelMap){
        initCtxMap(modelMap);
        return tableName +"/"+ page;
    }


}
