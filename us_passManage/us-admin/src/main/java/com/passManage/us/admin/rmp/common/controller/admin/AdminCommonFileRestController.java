package com.passManage.us.admin.rmp.common.controller.admin;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.passManage.us.admin.common.config.UploadFilePathConfig;
import com.passManage.us.core.common.exception.BusinessException;
import com.passManage.us.core.serialize.CommonReturnCode;
import com.passManage.us.core.serialize.ResponseMsg;
import com.passManage.us.core.utils.ListUtil;
import com.passManage.us.core.utils.MapUtil;
import com.passManage.us.core.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by magicalcoder.com on 2015/9/8.
 * 799374340@qq.com
 */
@RestController
@RequestMapping(value="/admin/common_file_rest/")
public class AdminCommonFileRestController {

    @Resource
    private UploadFilePathConfig uploadFilePathConfig;

    private String originFilePath(String originFile){
        if(StringUtil.isBlank(originFile) || originFile.startsWith("http://") || originFile.startsWith("https://")){
           return "";
        }
        String[] arr = originFile.split("/");
        StringBuilder pathBuilder = new StringBuilder();
        for(int i=0;i<arr.length;i++){
            if(i!=arr.length-1){
                pathBuilder.append(arr[i]).append("/");
            }
        }

        String path = pathBuilder.toString();
        String extraPrefix = uploadFilePathConfig.getFileExtraAddPrefix();

        String prefix = extraPrefix;
        if(StringUtil.isNotBlank(prefix)){
            if(path.startsWith(prefix)){
                path = path.substring(prefix.length());//把虚拟前缀都去掉
            }
        }
        if("/".equals(path)){
            path = "";
        }
        return path;
    }

    /**
     *
     * @param file 上传的新文件流
     * @param originFile 原始文件路径
     * @param from 来源是否是富文本  可以自行处理返回给富文本的值
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public ResponseMsg fileUpload(@RequestParam MultipartFile[] file,
                                  @RequestParam(required = false,value = "originFile") String originFile,
                                  @RequestParam(required = false,value = "from") String from,
                                  HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String originFilePath = originFilePath(originFile);
        String realPath = uploadFilePathConfig.getUploadDiskFolder()+ originFilePath ;
        //如果文件夹不存在就新建一个文件夹 聪明的根据当前目录规则来进行上传
        File dirPath = new File(realPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        String originalFilename = null;
        List<Map> returnUrls = new ArrayList<>();
        for (MultipartFile myfile : file) {
            if (!myfile.isEmpty()) {
                // 获取文件名
                originalFilename = myfile.getOriginalFilename();
                String returnUrl = "";
                boolean streamUsed = false;
                File storeFile = null;
                // 文件名后缀处理 使用uuid方式生成新文件名称 绝对唯一
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + suffix;
                if(uploadFilePathConfig.getUseDisk()){//本地路径
                    storeFile = new File(realPath, newFileName);
                    if(!storeFile.getParentFile().exists()){
                        storeFile.getParentFile().mkdirs();
                    }
                    FileUtils.copyInputStreamToFile(
                        myfile.getInputStream(), storeFile);//上传文件到磁盘 流会被拷贝走
                    streamUsed = true;
                    String prefix = uploadFilePathConfig.getFileExtraAddPrefix() +originFilePath;
                    String src = prefix+newFileName;
                    while (src.contains("//")){
                        src = src.replace("//","/");
                    }
                    if(uploadFilePathConfig.getUseDiskReturnUrl()){
                        returnUrl = src;
                    }
                }
                if(uploadFilePathConfig.getUseAliyunOss()){//阿里云路径
                    if(streamUsed){//那就上传本地文件
                        uploadFilePathConfig.ossClient().putObject(uploadFilePathConfig.getBucketName(),newFileName,storeFile);
                    }else {//直接使用客户端流 用户未上传过给本地文件
                        uploadFilePathConfig.ossClient().putObject(uploadFilePathConfig.getBucketName(),newFileName,myfile.getInputStream());
                    }
                    if(uploadFilePathConfig.getUseAliyunOssReturnUrl()){
                        returnUrl = uploadFilePathConfig.getAliyunImgDomain()+newFileName;
                    }
                }
                returnUrls.add(MapUtil.buildMap("src",returnUrl,"title",originalFilename));
            }
        }
        if(ListUtil.isNotBlank(returnUrls)){
            if(returnUrls.size()==1){//单个文件上传 返回1个地址
                return new ResponseMsg(returnUrls.get(0));
            }
            //多个文件 返回多个地址
            return new ResponseMsg(returnUrls);
        }
        throw new BusinessException(CommonReturnCode.FILE_UPLOAD_NO_FILE);
    }

}
