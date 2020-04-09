package com.passManage.us.core.service;

import com.passManage.us.core.common.exception.BusinessException;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.utils.POIUtil;
import com.passManage.us.core.utils.ReflectUtil;
import com.passManage.us.core.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 *
 * date:2018/9/7
 * function: 封装controller的一些通用方法
 */
public class CommonRestController<E,P> {
    public ICommonService<E,P> commonService;
    //查询类型
    protected int QUERY_TYPE_SEARCH=0;//普通搜索
    protected int QUERY_TYPE_EXPORT_EXCEL=1;//导出excel

    public String primaryKey;//主键名称

    //完全为了解决layui的where条件不支持动态改变值，会记住上一次的参数值
    public String coverBlankToNull(String input){
        if("".equals(input)){
            return null;
        }
        return input;
    }

    /**
     * 根据主键获取
     * @param id
     * @return
     */
    @RequestMapping(value="get/{id}", method={RequestMethod.GET})
    public ResponseMsg get(@PathVariable P id) {
        return new ResponseMsg(this.commonService.getModel(id));
    }
    /**
     * 保存
     * @return
     */
    @RequestMapping(value="save", method={RequestMethod.POST})
    public ResponseMsg save(@ModelAttribute E entity) {
        ReflectUtil<P,E> util = new ReflectUtil<>();
        P id = util.getBeanValue(entity,primaryKey);
        if(id instanceof String){
            if(StringUtil.isBlank((String)id)){
                id = null;
            }
        }
        if(id==null){
            //如果您希望程序自动维护创建时间 更新时间 请取消下面的注释
            /*autoSetCreateTime(entity);
            autoSetUpdateTime(entity);*/
            this.commonService.insertModel(entity);
        }else{
            E dbEntity = this.commonService.getModelNotIgnore(id,primaryKey);
            if(dbEntity==null){//
                /*autoSetCreateTime(entity);
                autoSetUpdateTime(entity);*/
                this.commonService.insertModel(entity);
            }else {
                /*autoSetUpdateTime(entity);*/
                this.commonService.updateModel(entity);
            }
        }
        return new ResponseMsg();
    }

    private void autoSetCreateTime(E entity){
        ReflectUtil<Timestamp,E> timeReflect = new ReflectUtil<>();
        try {//尝试下更新创建时间
            timeReflect.setBeanValue(entity,Timestamp.class,"createTime",new Timestamp(System.currentTimeMillis()));
        }catch (Exception e){
            //忽略日志
        }
    }
    private void autoSetUpdateTime(E entity){
        ReflectUtil<Timestamp,E> timeReflect = new ReflectUtil<>();
        try {//尝试下更新更改时间
            timeReflect.setBeanValue(entity,Timestamp.class,"updateTime",new Timestamp(System.currentTimeMillis()));
        }catch (Exception e){
            //忽略异常 有些场景没有时间字段 没必要报错
        }
    }
    /**
     * 更新记录
     * @param magicalCoderId
     * @return
     */
    //虽然id会智能赋值但是
    @RequestMapping(value="update/{magicalCoderId}", method={RequestMethod.POST})
    public ResponseMsg update(@PathVariable P magicalCoderId,@ModelAttribute E entity) {
        if(magicalCoderId==null){
            throw new BusinessException(null);
        }
//设置主键
        Class<P> clazz = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        ReflectUtil<P,E> util = new ReflectUtil<>();
        util.setBeanValue(entity,clazz,primaryKey,magicalCoderId);
        this.commonService.updateModelWithoutNull(entity);
        return new ResponseMsg();
    }

    /**
     * 根据主键删除单条记录
     * @param id
     * @return
     */
    @RequestMapping(value="delete/{id}", method={RequestMethod.POST})
    public ResponseMsg delete(@PathVariable P id) {
        this.commonService.deleteModel(id);
        return new ResponseMsg();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value="batch_delete" ,method={RequestMethod.POST})
    public ResponseMsg batchDelete(@RequestParam(value = "ids[]") Set<P> ids) {//ids是前端写死的入参 请不要改动
        this.commonService.deleteModel(ids);
        return new ResponseMsg();
    }

    /**
     * 导出excel
     * @param response
     * @param dataList 数据集合
     * @param fileNamePrefix 导出文件前缀
     * @param titles 标题
     * @param mappingArr 如遇下拉 给出下拉映射值
     */
    protected void exportExcel(HttpServletResponse response,List<E> dataList,String fileNamePrefix,String[] titles, String[] mappingArr){
        try {
            if(fileNamePrefix.length()<3){
                fileNamePrefix = fileNamePrefix+"magicalcoder";
            }
            File tmpFile = File.createTempFile(fileNamePrefix,".xlsx");
            POIUtil<E> poiUtil = new POIUtil<E>();
            poiUtil.writeExcelToTempFile(dataList
                ,titles
                ,mappingArr
                ,tmpFile);
            toFile(response, tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件流输出
     * @param response
     * @param file 文件
     */
    protected void toFile(HttpServletResponse response, File file) {
        try {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[256];
            out = response.getOutputStream();
            while((len = in.read(buffer)) > 0) {
                out.write(buffer,0,len);
            }
        }catch(Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(in != null) {
                try {
                    in.close();
                }catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
