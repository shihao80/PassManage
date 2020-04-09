package com.passManage.us.admin.rmp.common.controller.admin.sys;

import com.passManage.us.admin.rmp.model.SysModuleCategory;
import com.passManage.us.admin.rmp.service.SysModuleCategoryService;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.service.CommonRestController;
import com.passManage.us.core.utils.ListUtil;
import com.passManage.us.core.utils.StringUtil;
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

@RequestMapping("/admin/sys_module_category_rest/")
@RestController
public class AdminSysModuleCategoryRestController extends CommonRestController<SysModuleCategory,Long> implements InitializingBean
{

    @Resource
    private SysModuleCategoryService sysModuleCategoryService;

        @RequestMapping(value = "search")
    public ResponseMsg search(
            @RequestParam(required = false) String uniqueField,
            @RequestParam(required = false,value = "uniqueValue[]") Set<Long> uniqueValue,//主键多个值
            @RequestParam(required = false,defaultValue = "20") Integer limit,
            @RequestParam(required = false) String keyword
    ){
        limit = Math.min(PageConstant.MAX_LIMIT,limit);
        List<SysModuleCategory> list = null;
        Map<String,Object> query = new HashMap();
        query.put("limit",limit);
        query.put("notSafeOrderBy","sort_num asc");
        if(uniqueValue!=null){//说明是来初始化的
            list = sysModuleCategoryService.getModelInList(uniqueValue);
        }else {//正常搜索
            query.put("moduleCategoryNameFirst",keyword);
            list = sysModuleCategoryService.getModelList(query);
            query.remove("moduleCategoryNameFirst");
        }
        return new ResponseMsg(list);
    }
    //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="moduleCategoryNameFirst")                            String moduleCategoryNameFirst ,
        @RequestParam int page,@RequestParam int limit,@RequestParam(required = false) String safeOrderBy
        ,HttpServletResponse response,@RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("moduleCategoryNameFirst",coverBlankToNull(moduleCategoryNameFirst));
        Integer count = sysModuleCategoryService.getModelListCount(query);
        if(StringUtil.isBlank(safeOrderBy)){
            query.put("notSafeOrderBy","sort_num asc");
        }else{
            query.put("safeOrderBy",safeOrderBy);
        }
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,sysModuleCategoryService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,sysModuleCategoryService.getModelList(query),"一级菜单",
            new String[]{"主键","一级菜单名称","排序"},
            new String[]{"","",""});
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = sysModuleCategoryService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
