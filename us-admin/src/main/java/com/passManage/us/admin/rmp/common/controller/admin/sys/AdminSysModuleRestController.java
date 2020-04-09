package com.passManage.us.admin.rmp.common.controller.admin.sys;

import com.passManage.us.admin.rmp.constant.PermissionConstant;
import com.passManage.us.admin.rmp.model.SysModule;
import com.passManage.us.admin.rmp.model.SysPermission;
import com.passManage.us.admin.rmp.service.SysModuleService;
import com.passManage.us.admin.rmp.service.SysPermissionService;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.service.CommonRestController;
import com.passManage.us.core.utils.ListUtil;
import com.passManage.us.core.utils.MapUtil;
import com.passManage.us.core.utils.ReflectUtil;
import com.passManage.us.core.utils.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.*;

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

@RequestMapping("/admin/sys_module_rest/")
@RestController
public class AdminSysModuleRestController extends CommonRestController<SysModule,Long> implements InitializingBean
{

    @Resource
    private SysModuleService sysModuleService;


        @RequestMapping(value = "search")
    public ResponseMsg search(
            @RequestParam(required = false) String uniqueField,
            @RequestParam(required = false,value = "uniqueValue[]") Set<Long> uniqueValue,//主键多个值
            @RequestParam(required = false,defaultValue = "20") Integer limit,
            @RequestParam(required = false) String keyword
    ){
        limit = Math.min(PageConstant.MAX_LIMIT,limit);
        List<SysModule> list = null;
        Map<String,Object> query = new HashMap();
        query.put("limit",limit);
        query.put("notSafeOrderBy","sort_num asc");
        if(uniqueValue!=null){//说明是来初始化的
            list = sysModuleService.getModelInList(uniqueValue);
        }else {//正常搜索
            query.put("moduleTitleFirst",keyword);
            list = sysModuleService.getModelList(query);
            query.remove("moduleTitleFirst");
        }
        return new ResponseMsg(list);
    }
    //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="moduleNameFirst")                            String moduleNameFirst ,
        @RequestParam(required = false,value ="moduleCategoryIdFirst")                            Long moduleCategoryIdFirst ,
        @RequestParam(required = false,value ="moduleTitleFirst")                            String moduleTitleFirst ,
        @RequestParam int page,@RequestParam int limit,@RequestParam(required = false) String safeOrderBy
        ,HttpServletResponse response,@RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("moduleNameFirst",coverBlankToNull(moduleNameFirst));
        query.put("moduleCategoryIdFirst",moduleCategoryIdFirst);
        query.put("moduleTitleFirst",coverBlankToNull(moduleTitleFirst));
        Integer count = sysModuleService.getModelListCount(query);
        if(StringUtil.isBlank(safeOrderBy)){
            query.put("notSafeOrderBy","sort_num asc");
        }else{
            query.put("safeOrderBy",safeOrderBy);
        }
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,sysModuleService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,sysModuleService.getModelList(query),"模块",
            new String[]{"主键","二级菜单唯一值","二级菜单地址","一级菜单","排序","二级菜单标题","是否显示"},
            new String[]{"","","","","","",""});
        }
        return null;
    }


    /**
     * 保存
     * @return
     */
    @RequestMapping(value="extra_save", method={RequestMethod.POST})
    public ResponseMsg saveEntity(@RequestParam(required = false)Boolean helpConfigPermit ,@ModelAttribute SysModule entity) {
        this.sysModuleService.saveModule(helpConfigPermit,entity);
        return new ResponseMsg();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = sysModuleService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
