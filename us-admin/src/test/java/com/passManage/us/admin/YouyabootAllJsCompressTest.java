package com.passManage.us.admin;

import com.passManage.us.core.common.file.FileHelper;
import com.yahoo.platform.yui.compressor.YUICompressor;

import java.io.File;
import java.util.List;

/**
    自动压缩合并us-all.js;
    为了获取更高的性能。当发布上线时，us加载单独的1个js 避免加载多个js导致加载速度慢的问题
 */
public class YouyabootAllJsCompressTest {
    public static void main(String[] args) {
        String path = YouyabootAllJsCompressTest.class.getResource("/").getPath();
        File file = new File(path);
        String usAdminPath = file.getParentFile().getParentFile().getAbsolutePath();
        String crudPath = String.format("%s\\src\\main\\resources\\static\\assets\\magicalcoder\\v103\\crud\\",usAdminPath).replace("\\",File.separator);
        String[] needCombine = needCombineFiles(crudPath);
        String minPath = crudPath+File.separator+"min"+File.separator;
        File minFile = new File(minPath);
        if(!minFile.exists()){
            minFile.mkdirs();
        }
        //压缩
        int tmp = 1;
        for(String needFile:needCombine){
            String[] p = new String[]{
                "--type", "js",
                "--charset", "utf-8",
                needFile,
                "-o", minPath+(tmp++)+".js",
            };

            YUICompressor.main(p);
        }
        //合并
        List<File> minFiles = FileHelper.listAllFile(minPath);
        StringBuilder sb = new StringBuilder("/*制作方法参考YouyabootAllJsCompressTest.java*/\r\n");
        for(File min:minFiles){
            sb.append(FileHelper.fastReadAllLineFile(min,"UTF-8"));
            min.delete();
        }
        String usAllMinJs = crudPath+"youya-boot-all.min.js";
        FileHelper.fastWriteFileUTF8(usAllMinJs,sb.toString());
        System.out.println("youya-boot-all.min.js制作完成");
    }
    /*如果有新增 请自行添加 因为考虑到加载顺序问题，就不自动读取文件了*/
    private static String[] needCombineFiles(String crudPath){
        String[] arr = new String[8];
        arr[0] = crudPath + coverPathSplit("lib\\authtree.js");

        arr[2] = crudPath + coverPathSplit("core\\mc-constant.js");
        arr[1] = crudPath + coverPathSplit("core\\mc-util.js");
        arr[3] = crudPath + coverPathSplit("core\\mc-verify.js");

        arr[4] = crudPath + coverPathSplit("component\\mc-select2.js");
        arr[5] = crudPath + coverPathSplit("component\\mc-rmp.js");
        arr[6] = crudPath + coverPathSplit("component\\mc-layui-component.js");
        arr[7] = crudPath + coverPathSplit("component\\mc-children.js");

        return arr;
    }

    private static String coverPathSplit(String path){
        return path.replace("\\",File.separator);
    }
}
