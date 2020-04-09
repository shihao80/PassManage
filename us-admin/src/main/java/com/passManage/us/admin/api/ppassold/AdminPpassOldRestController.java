package com.passManage.us.admin.api.ppassold;

import com.passManage.us.core.service.CommonRestController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import com.passManage.us.core.common.constant.PageConstant;
import com.passManage.us.core.common.exception.BusinessException;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.model.PpassOld;
import com.passManage.us.service.ppassold.service.PpassOldService;

import com.passManage.us.core.utils.ListUtil;
import com.passManage.us.core.utils.MapUtil;
import com.passManage.us.core.utils.StringUtil;


/**
* 代码为自动生成 Created by www.magicalcoder.com
* 软件作者：何栋宇 qq:709876443
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:648595928
*/

@RequestMapping("/admin/p_pass_old_rest/")
@RestController
public class AdminPpassOldRestController extends CommonRestController<PpassOld,Integer> implements InitializingBean
{

    @Resource
    private PpassOldService ppassOldService;

        //分页查询
    @RequestMapping(value={"page"}, method={RequestMethod.GET})
    public ResponseMsg page(
        @RequestParam(required = false,value ="passIdFirst")                            Integer passIdFirst ,
        @RequestParam(required = false,value ="passNameFirst")                            String passNameFirst ,
        @RequestParam(required = false,value ="passLengthFirst")                            Integer passLengthFirst ,
        @RequestParam(required = false,value ="passTypeFirst")                            String passTypeFirst ,
        @RequestParam(required = false,value ="passExpiryFirst")                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date passExpiryFirst ,
        @RequestParam(required = false,value ="passExpirySecond")                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date passExpirySecond ,
        @RequestParam(required = false,value ="passCreatetimeFirst")                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date passCreatetimeFirst ,
        @RequestParam(required = false,value ="passCreatetimeSecond")                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date passCreatetimeSecond ,
        @RequestParam(required = false,value ="passUseridFirst")                            Integer passUseridFirst ,
        @RequestParam int page,@RequestParam int limit,@RequestParam(required = false) String safeOrderBy
        ,HttpServletResponse response,@RequestParam(required = false) Integer queryType
    ){
        Map<String,Object> query = new HashMap();
        query.put("passIdFirst",passIdFirst);
        query.put("passNameFirst",coverBlankToNull(passNameFirst));
        query.put("passLengthFirst",passLengthFirst);
        query.put("passTypeFirst",coverBlankToNull(passTypeFirst));
        query.put("passExpiryFirst",passExpiryFirst);
        query.put("passExpirySecond",passExpirySecond);
        query.put("passCreatetimeFirst",passCreatetimeFirst);
        query.put("passCreatetimeSecond",passCreatetimeSecond);
        query.put("passUseridFirst",passUseridFirst);
        Integer count = ppassOldService.getModelListCount(query);
        if(StringUtil.isBlank(safeOrderBy)){
            query.put("notSafeOrderBy","pass_id desc");
        }else{
            query.put("safeOrderBy",safeOrderBy);
        }
        if(queryType==null || queryType == QUERY_TYPE_SEARCH){//普通查询
            limit = Math.min(limit, PageConstant.MAX_LIMIT);
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            return new ResponseMsg(count,ppassOldService.getModelList(query));
        }else if(queryType == QUERY_TYPE_EXPORT_EXCEL){
            query.put("start",(page - 1) * limit);query.put("limit",limit);
            exportExcel(response,ppassOldService.getModelList(query),"p_pass_old",
            new String[]{"密钥UUID","密钥名称","密钥长度","密钥类型","第一子密钥","第二子密钥","第三子密钥","密钥有效期","密钥上传时间","密钥使用者"},
            new String[]{"","","","","","","","","",""});
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.commonService = ppassOldService;
        super.primaryKey = "passId";//硬编码此实体的主键名称
    }
}
