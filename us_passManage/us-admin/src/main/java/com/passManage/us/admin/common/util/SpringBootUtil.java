package com.passManage.us.admin.common.util;


import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * 探知启动jar所在的路径 或者classpath
 */
public class SpringBootUtil {
    //获取jar的所在的路径文件
    public static File getJarDir() {
        ApplicationHome home = new ApplicationHome(SpringBootUtil.class);
        return home.getDir();
    }
    //获取jar的所在的路径文件字符串
    public static String getJarDirPath() {
        ApplicationHome home = new ApplicationHome(SpringBootUtil.class);
        return home.getDir().getAbsolutePath();
    }
}
