package com.passManage.us.core.utils;

import java.io.File;

/**
 * Created by hedongyu on 2018/7/23.
 * 打开文件夹
 */
public class OpenDiskUtil {
    public static void openURL(String url) {
        try {
            browse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openFileDir(File file) {
        try {
            browse(file.getParentFile().getAbsolutePath()+File.separator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void browse(String url) throws Exception {
        //获取操作系统的名字
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            //苹果的打开方式
            Runtime.getRuntime().exec(" " + url);
        } else if (osName.startsWith("Windows")) {
            //windows的打开方式。
            Runtime.getRuntime().exec("explorer " + url);
        } else {
            // Unix or Linux的打开方式
            Runtime.getRuntime().exec("nautilus " + url);
        }
    }
}
