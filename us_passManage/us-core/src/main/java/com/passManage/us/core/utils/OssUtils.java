package com.passManage.us.core.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Created by asus on 2019/6/26.
 * oos 工具类 用于初始化连接oos 以及关闭oos连接
 */
public class OssUtils {

    /**Region请按实际情况填写 */
    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    /**oos存储账号*/
    private static String accessKeyId = "";
    /**oos存储密码*/
    private static String accessKeySecret = "";
    /**oos存储桶*/
    private static String bucketName = "us";

    public static void main(String[] args) {
        initOos();
        upload(initOos(),System.currentTimeMillis()+"","E:\\timg.jpg");
    }

    /**初始化oos连接 返回一个连接对象*/
    public static OSSClient initOos(){
        /**创建一个ossClient实例*/
        OSSClient ossClient =null;
        /** 使用异常用于处理 ： 连接失败、连接异常等原因*/
        try {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }catch (Exception e){
            /**用于接受连接异常*/
            System.out.println("创建oos实例失败");
        }
        return ossClient;
    }

    /**上传本地文件*/
    public static boolean upload(OSSClient ossClient,String objectName,String path){

        try{
            /**需要判断本地文件是否存在 如果不存在上传失败
             并且需要加上异常 由于网速或者其他原因导致上传失败 需要异常处理*/
            PutObjectResult result = ossClient.putObject(bucketName, objectName, new File(path));
            System.out.println(JSON.toJSONString(result));
        }
        catch (Exception e){
            /**如果出现异常 返回上传失败*/
            return false;
        }
        /**上传成功*/
        return true;
    }

    /**上传网络文件*/
    public static boolean upload1(OSSClient ossClient,String objectName,String fileUrl){

        try{
            /**上传网络流 需要加上异常 由于网速或者其他原因导致上传失败 需要异常处理*/
            InputStream inputStream = new URL(fileUrl).openStream();
            ossClient.putObject(bucketName, objectName, inputStream);
        }
        catch (Exception e){
            /**如果出现异常 返回上传失败*/
            return false;
        }
        /**上传成功*/
        return true;
    }


    /**获取文件url*/
    public static URL getFileUrl(OSSClient ossClient,String objectName) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        //这个地方需要判断 URL是否可用 如果可用 表示 OOS中存在文件 否则不存在文件
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        return url;
    }

    /**关闭连接oos对象 释放资源 防止占用连接*/
    public static void destroy( OSSClient ossClient){
        /** 使用异常用于处理 ：关闭oos连接*/
        try {
            ossClient.shutdown();
        }catch (Exception e){
            /**用于接受连接异常*/
            System.out.println("关闭oos实例失败");
        }
    }

    /**判断url是否可用*/
    public static boolean handleUrl(String fileUrl){
        URL url;
        try {
            url = new URL(fileUrl);
            InputStream in = url.openStream();
            return true;
        } catch (Exception e1) {
            url = null;
            return false;
        }
    }


}
