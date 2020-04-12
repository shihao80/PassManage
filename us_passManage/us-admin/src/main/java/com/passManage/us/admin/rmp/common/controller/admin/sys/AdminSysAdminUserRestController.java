package com.passManage.us.admin.rmp.common.controller.admin.sys;

import com.passManage.us.admin.rmp.common.SecurityReturnCode;
import com.passManage.us.admin.rmp.model.SysAdminUser;
import com.passManage.us.admin.rmp.service.SysAdminUserService;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.common.exception.BusinessException;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.service.CommonRestController;
import com.passManage.us.core.utils.ListUtil;
import com.passManage.us.core.utils.Md5Util;
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
* 欢迎加入官方QQ群:323237052
*/

@RequestMapping("/admin/sys_admin_user_rest/")
@RestController
public class AdminSysAdminUserRestController extends CommonRestController<SysAdminUser,Long> implements InitializingBean
{

    @Resource
    private SysAdminUserService sysAdminUserService;

        @RequestMapping(value = "search")
    public ResponseMsg search(
            @RequestParam(required = false) String uniqueField,
            @RequestParam(required = false,value = "uniqueValue[]") Set<Long> uniqueValue,//主键多个值
            @RequestParam(required = false,defaultValue = "20") Integer limit,
            @RequestParam(required = false) String keyword
    ){
        limit = Math.min(PageConstant.MAX_LIMIT,limit);
        List<SysAdminUser> list = null;
        Map<String,Object> query = new HashMap();
        query.put("limit",limit);
        if(uniqueValue!=null){//说明是来初始化的
            list = sysAdminUserService.getModelInList(uniqueValue);
        }else {//正常搜索
            query.put("usernameFirst",keyword);
            list = sysAdminUserService.getModelList(query);
            query.remove("usernameFirst");
        }
        return new ResponseMsg(list);
    }
    //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="usernameFirst")                            String usernameFirst ,
        @RequestParam(required = false,value ="roleIdFirst")                            Long roleIdFirst ,
        @RequestParam int page, @RequestParam int limit, @RequestParam(required = false) String safeOrderBy
        , HttpServletResponse response, @RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("usernameFirst",coverBlankToNull(usernameFirst));
        query.put("roleIdFirst",roleIdFirst);
        Integer count = sysAdminUserService.getModelListCount(query);
        query.put("safeOrderBy",safeOrderBy);
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,sysAdminUserService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,sysAdminUserService.getModelList(query),"管理员",
            new String[]{"主键","用户名","密码","真名","邮箱","座机号","手机号","地址","创建时间","更新时间","角色","是否未失效","是否未锁定","是否未失效","是否可用"},
            new String[]{"","","","","","","","","","","","","","",""});
        }
        return null;
    }

    @RequestMapping(value="save", method={RequestMethod.POST})
    public ResponseMsg save(@ModelAttribute SysAdminUser entity) {
        /*{保留我*/
        if(StringUtil.isBlank(entity.getPassword())){
            throw new BusinessException(SecurityReturnCode.PASSWORD_NULL);
        }
        if(!entity.getPassword().matches("[a-z0-9]{32}")){//非系统生成的密码进行加密
            entity.setPassword(Md5Util.md5Encode(entity.getPassword()));
        }
        /*}*/
        if(entity.getId()==null){
            this.sysAdminUserService.insertModel(entity);
        }else{
            this.sysAdminUserService.updateModel(entity);
        }
        return new ResponseMsg();
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = sysAdminUserService;
        super.primaryKey = "id";//硬编码此实体的主键名称
    }
}
