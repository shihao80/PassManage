package com.passManage.us.admin.rmp.common.controller.admin.sys;

import com.passManage.us.admin.rmp.model.SysPermission;
import com.passManage.us.admin.rmp.service.SysPermissionService;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.service.CommonRestController;
import com.passManage.us.core.utils.ListUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* 代码为自动生成 Created by www.magicalcoder.com
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:323237052
*/

@RequestMapping("/admin/sys_permission_rest/")
@RestController
public class AdminSysPermissionRestController extends CommonRestController<SysPermission,Long> implements InitializingBean
{

    @Resource
    private SysPermissionService sysPermissionService;

        @RequestMapping(value = "search")
    public ResponseMsg search(
            @RequestParam(required = false) String uniqueField,
            @RequestParam(required = false,value = "uniqueValue[]") Set<Long> uniqueValue,//主键多个值
            @RequestParam(required = false,defaultValue = "20") Integer limit,
            @RequestParam(required = false) String keyword
    ){
        limit = Math.min(PageConstant.MAX_LIMIT,limit);
        List<SysPermission> list = null;
        Map<String,Object> query = new HashMap();
        query.put("limit",limit);
        if(uniqueValue!=null){//说明是来初始化的
            list = sysPermissionService.getModelInList(uniqueValue);
        }else {//正常搜索
            query.put("idFirst",keyword);
            list = sysPermissionService.getModelList(query);
            query.remove("idFirst");
            if(ListUtil.isBlank(list)){
                query.put("permissionNameFirst",keyword);
                list = sysPermissionService.getModelList(query);
                query.remove("permissionNameFirst");
            }
        }
        return new ResponseMsg(list);
    }
    //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="idFirst")                            Long idFirst ,
        @RequestParam(required = false,value ="permissionNameFirst")                            String permissionNameFirst ,
        @RequestParam(required = false,value ="moduleIdFirst")                            Long moduleIdFirst ,
        @RequestParam int page, @RequestParam int limit, @RequestParam(required = false) String safeOrderBy
        , HttpServletResponse response, @RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("idFirst",idFirst);
        query.put("permissionNameFirst",coverBlankToNull(permissionNameFirst));
        query.put("moduleIdFirst",moduleIdFirst);
        Integer count = sysPermissionService.getModelListCount(query);
        query.put("safeOrderBy",safeOrderBy);
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,sysPermissionService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,sysPermissionService.getModelList(query),"权限明细配置",
            new String[]{"主键","规则适用端","权限名称","后端正则地址","DOM前端Key","前端处理方式","所属二级菜单"},
            new String[]{"","[{\"value\":\"请选择\",\"key\":\"\"},{\"value\":\"前端\",\"key\":\"front\"},{\"value\":\"后端\",\"key\":\"backend\"},{\"value\":\"前后端\",\"key\":\"front_backend\"}]","","","","[{\"value\":\"请选择\",\"key\":\"\"},{\"value\":\"读写\",\"key\":\"show\"},{\"value\":\"只读\",\"key\":\"disabled\"}]",""});
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = sysPermissionService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
