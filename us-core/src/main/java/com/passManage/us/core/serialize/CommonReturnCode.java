package com.passManage.us.core.serialize;

/**
 * 定义一些通用的错误码
 */
public class CommonReturnCode {

//	private CommonReturnCode(){}

    public static final KeyValuePair SUCCESS = new KeyValuePair(0, "成功","成功");

    public static final KeyValuePair EXCEPTION = new KeyValuePair(99991, "服务器异常","异常");

    public static final KeyValuePair FILE_UPLOAD_ERROR = new KeyValuePair(98001, "文件上传失败","文件上传失败");
    public static final KeyValuePair FILE_UPLOAD_SIZE_TO_BIG = new KeyValuePair(98002, "文件上传大小超过限制","文件上传大小超过限制");
    public static final KeyValuePair FILE_UPLOAD_NO_FILE = new KeyValuePair(98404, "入参没有文件","入参没有文件");



}
