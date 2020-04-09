package com.passManage.us.admin.rmp.common.controller.admin.sys;

import com.passManage.us.admin.rmp.model.SysLogAdminOperate;
import com.passManage.us.admin.rmp.service.SysLogAdminOperateService;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.service.CommonRestController;
import com.passManage.us.core.utils.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
* 代码为自动生成 Created by www.magicalcoder.com
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:323237052
*/

@RequestMapping("/admin/sys_log_admin_operate_rest/")
@RestController
public class AdminSysLogAdminOperateRestController extends CommonRestController<SysLogAdminOperate,Long> implements InitializingBean
{

    @Resource
    private SysLogAdminOperateService sysLogAdminOperateService;

        //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="adminUserIdFirst")                            Long adminUserIdFirst ,
        @RequestParam(required = false,value ="userNameFirst")                            String userNameFirst ,
        @RequestParam(required = false,value ="tableNameFirst")                            String tableNameFirst ,
        @RequestParam(required = false,value ="formBodyFirst")                            String formBodyFirst ,
        @RequestParam(required = false,value ="createTimeFirst")                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Timestamp createTimeFirst ,
        @RequestParam(required = false,value ="createTimeSecond")                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Timestamp createTimeSecond ,
        @RequestParam int page,@RequestParam int limit,@RequestParam(required = false) String safeOrderBy
        ,HttpServletResponse response,@RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("adminUserIdFirst",adminUserIdFirst);
        query.put("userNameFirst",coverBlankToNull(userNameFirst));
        query.put("tableNameFirst",coverBlankToNull(tableNameFirst));
        query.put("formBodyFirst",coverBlankToNull(formBodyFirst));
        query.put("createTimeFirst",createTimeFirst);
        query.put("createTimeSecond",createTimeSecond);
        Integer count = sysLogAdminOperateService.getModelListCount(query);
        query.put("safeOrderBy",safeOrderBy);
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,sysLogAdminOperateService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,sysLogAdminOperateService.getModelList(query),"系统日志",
            new String[]{"主键","管理员","管理员名称","表名","操作类型","链接","主键值","提交内容","创建时间"},
            new String[]{"","","","","","","","",""});
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = sysLogAdminOperateService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
