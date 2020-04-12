package com.passManage.us.core.utils;

import com.alibaba.fastjson.JSON;
import com.passManage.us.core.common.dto.KeyValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by magicalcoder.com
 * 引入excel v1.0.1版本新增此工具类 2018/12/20 如不需要您也可以删除
 */
public class POIUtil<T>{

    /**
     * 导出excel到临时文件
     * @param pageList 集合对象
     * @param titles   第一行标题
     * @param mappingArr  映射：[{"key":"0","value":"类型0"},{"key":"1","value":"类型1"}],....
     * @param tmpFile  导出的excel文件
     */
    public void writeExcelToTempFile(List<T> pageList, String[] titles, String[] mappingArr, File tmpFile) {
        FileOutputStream out = null;
        SXSSFWorkbook wb = new SXSSFWorkbook(1000); // keep 100 rows in memory, exceeding rows will be flushed to disk
        Sheet sh = wb.createSheet();
        try {
            setValidation(pageList, mappingArr,sh);
            //头部 第1行
            Row headRow = sh.createRow(0);
            for(int i=0;i<titles.length;i++){
                Cell cell = headRow.createCell(i);
                cell.setCellValue(titles[i]);
            }
            Map<Integer,Map<String,String>> mappingMap = buildMappingMergeMap(mappingArr);
            //数据
            for(int i=0;i<pageList.size();i++){
                Object obj = pageList.get(i);
                Row row = sh.createRow(i+1);
                Field[] fields=obj.getClass().getDeclaredFields();
                for(int j=0;j<fields.length;j++){
                    Map<String,String> map = mappingMap.get(j);
                    Field field = fields[j];
                    Cell cell = row.createCell(j);
                    field.setAccessible(true);
                    Object dbValue = field.get(obj);
                    if(dbValue != null){
                        String magicalValue = map!=null?map.get(String.valueOf(dbValue)):null;//比较友好的值 比如下拉文本
                        if(magicalValue!=null){
                            cell.setCellValue(magicalValue);
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            continue;
                        }
                        String type = field.getType().getSimpleName();
                        if(type.equals("Date")){
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            magicalValue = DateFormatUtil.getStringDate((Date)dbValue,"yyyy-MM-dd HH:mm:ss");
                            cell.setCellValue(magicalValue);
                        }else if(type.equals("Boolean") || type.equals("boolean")){
                            if(magicalValue==null){
                                magicalValue = map!=null?map.get(String.valueOf((Boolean) dbValue ? 1 : 0)):null;
                            }
                            if(magicalValue!=null){
                                cell.setCellValue(magicalValue);
                            }else {
                                cell.setCellValue(String.valueOf(dbValue));
                            }
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        }else if(type.equals("Long")|| type.equals("long")){
                            cell.setCellValue((Long)(dbValue));
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else if(type.equals("Integer")|| type.equals("int")){
                            cell.setCellValue((Integer)dbValue);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else if(type.equals("Byte")|| type.equals("byte")){
                            cell.setCellValue((Byte)dbValue);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else if(type.equals("byte[]")){
                            cell.setCellValue(new String((byte[]) dbValue,"UTF-8"));
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        }else if(type.equals("Double")|| type.equals("double")){
                            cell.setCellValue((Double)dbValue);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else if(type.equals("Float")|| type.equals("float")){
                            cell.setCellValue((Float)dbValue);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else if(type.equals("BigDecimal")){
                            cell.setCellValue(((BigDecimal)dbValue).floatValue());
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else {
                            cell.setCellValue(String.valueOf(dbValue));
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        }
                    }
                }
            }
            out = new FileOutputStream(tmpFile);
            wb.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(wb!=null){
                wb.dispose();
            }
        }
    }
    //转化成表头定制的下拉约束
    private static String[] mappingToSelectList(String mapping){
        if(StringUtil.isBlank(mapping)){
            return null;
        }
        List<KeyValue> list = null;
        try {
            list = JSON.parseArray(mapping,KeyValue.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(ListUtil.isBlank(list)){
            return null;
        }
        String[] selectList = new String[list.size()];
        for(int i=0;i<list.size();i++){
            KeyValue itemMap = list.get(i);
            selectList[i]=itemMap.getValue();
        }
        return selectList;
    }
    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     * @param sheet 要设置的sheet.
     * @return 设置好的sheet.
     */
    public static <T> Sheet setValidation(List<T> pageList, String[] mappingArr, Sheet sheet) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        int size = pageList.size();
        for(int i=0;i<mappingArr.length;i++){
            if(StringUtil.isNotBlank(mappingArr[i])){
                String[] selectList = mappingToSelectList(mappingArr[i]);
                if(selectList!=null && selectList.length>0){
                    CellRangeAddressList addressList = new CellRangeAddressList();
                    addressList.addCellRangeAddress(1, i,size, i);
                    //设置下拉框数据
                    DataValidationConstraint constraint = helper.createExplicitListConstraint(selectList);
                    DataValidation dataValidation = helper.createValidation(constraint, addressList);
                    //处理Excel兼容性问题
                    if(dataValidation instanceof XSSFDataValidation) {
                        dataValidation.setSuppressDropDownArrow(true);
                        dataValidation.setShowErrorBox(true);
                    }else {
                        dataValidation.setSuppressDropDownArrow(false);
                    }
                    sheet.addValidationData(dataValidation);
                }
            }
        }
        return sheet;
    }

    /**
     * key 行 value 需要构造的下拉源数据
     * @param mappingArr
     * @return
     */
    private  Map<Integer,Map<String,String>> buildMappingMergeMap(String[] mappingArr){
        Map<Integer,Map<String,String>> mappingMap = new HashMap<Integer,Map<String,String>>();
        for(int i=0;i<mappingArr.length;i++){
            mappingMap.put(i,mappingMergeToMap(mappingArr[i]));
        }
        return mappingMap;
    }
    //合并成map [{key:"a",value:"1"],{key:"b",value:"2"]} -> {"a":"1","b":"2"}
    private Map<String,String> mappingMergeToMap(String mapping){
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtil.isBlank(mapping)){
            return map;
        }
        try{
            List<KeyValue> list = JSON.parseArray(mapping,KeyValue.class);
            if(ListUtil.isNotBlank(list)){
                for(int i=0;i<list.size();i++){
                    KeyValue keyValue = list.get(i);
                    map.putAll(MapUtil.buildStringMap(keyValue.getKey(),keyValue.getValue()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
