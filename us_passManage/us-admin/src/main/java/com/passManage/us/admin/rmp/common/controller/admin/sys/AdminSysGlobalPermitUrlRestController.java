package com.passManage.us.admin.rmp.common.controller.admin.sys;

import com.passManage.us.admin.rmp.model.SysGlobalPermitUrl;
import com.passManage.us.admin.rmp.service.SysGlobalPermitUrlService;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.service.CommonRestController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;


/**
* 代码为自动生成 Created by www.magicalcoder.com
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:323237052
*/

@RequestMapping("/admin/sys_global_permit_url_rest/")
@RestController
public class AdminSysGlobalPermitUrlRestController extends CommonRestController<SysGlobalPermitUrl,Long> implements InitializingBean
{

    @Resource
    private SysGlobalPermitUrlService sysGlobalPermitUrlService;

        //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="permitNameFirst")                            String permitNameFirst ,
        @RequestParam(required = false,value ="moduleIdFirst")                            Long moduleIdFirst ,
        @RequestParam int page,@RequestParam int limit,@RequestParam(required = false) String safeOrderBy
        ,HttpServletResponse response,@RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("permitNameFirst",coverBlankToNull(permitNameFirst));
        query.put("moduleIdFirst",moduleIdFirst);
        Integer count = sysGlobalPermitUrlService.getModelListCount(query);
        query.put("safeOrderBy",safeOrderBy);
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,sysGlobalPermitUrlService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,sysGlobalPermitUrlService.getModelList(query),"后端请求地址全局允许过滤器,在此表的统一不用再去权限表匹配了",
            new String[]{"主键","通过名称","后端的地址正则","所属模块"},
            new String[]{"","","",""});
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = sysGlobalPermitUrlService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
