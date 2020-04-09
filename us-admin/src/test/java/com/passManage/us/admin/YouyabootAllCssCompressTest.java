package com.passManage.us.admin;

import com.passManage.us.core.common.file.FileHelper;
import com.yahoo.platform.yui.compressor.YUICompressor;

import java.io.File;
import java.util.List;

/**
    自动压缩合并us-all.css;
    为了获取更高的性能。当发布上线时，us加载单独的1个css 避免加载多个js导致加载速度慢的问题
 */
public class YouyabootAllCssCompressTest {
    public static void main(String[] args) {
        String path = YouyabootAllCssCompressTest.class.getResource("/").getPath();
        File file = new File(path);
        String usAdminPath = file.getParentFile().getParentFile().getAbsolutePath();
        String cssPath = String.format("%s\\src\\main\\resources\\static\\assets\\magicalcoder\\v103\\css\\",usAdminPath).replace("\\",File.separator);
        String[] needCombine = needCombineFiles(cssPath);
        String minPath = cssPath+File.separator+"min"+File.separator;
        //压缩
        int tmp = 1;
        for(String needFile:needCombine){
            String[] p = new String[]{
                "--type", "css",
                "--charset", "utf-8",
                needFile,
                "-o", minPath+(tmp++)+".css",
            };

            YUICompressor.main(p);
        }
        //合并
        List<File> minFiles = FileHelper.listAllFile(minPath);
        StringBuilder sb = new StringBuilder("/*制作方法参考YouyabootAllCssCompressTest.java*/\r\n");
        for(File min:minFiles){
            sb.append(FileHelper.fastReadAllLineFile(min,"UTF-8"));
            min.delete();
        }
        String usAllMinJs = cssPath+"youya-boot-all.min.css";
        FileHelper.fastWriteFileUTF8(usAllMinJs,sb.toString());
        System.out.println("youya-boot-all.min.css制作完成");
    }
    /*如果有新增 请自行添加 因为考虑到加载顺序问题，就不自动读取文件了*/
    private static String[] needCombineFiles(String crudPath){
        String[] arr = new String[3];
        arr[0] = crudPath + coverPathSplit("public.css");
        arr[2] = crudPath + coverPathSplit("select2.min.css");
        arr[1] = crudPath + coverPathSplit("magicalcoder.css");

        return arr;
    }

    private static String coverPathSplit(String path){
        return path.replace("\\",File.separator);
    }
}
