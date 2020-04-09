package com.passManage.us.core.utils;

import com.passManage.us.core.common.constant.SystemOperateEnum;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by hedongyu on 2018/7/23.
 * 打开浏览器
 */
public class OpenBrowserUtil {

    public static void main(String[] args) {
//        openURL("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe","http://www.baidu.com");
    }

    public static void openURL(String explorerPath,String url){
        if(StringUtil.isBlank(explorerPath)){
            if(openURL(url)){
                return;
            }
        }else {
            File file = new File(explorerPath);
            if(!file.exists()){
                openURL(url);
                return;
            }
            SystemOperateEnum systemOperateEnum = SystemOperateUtil.system();
            switch (systemOperateEnum){
                case WINDOWS:
                    try {
                        ProcessBuilder proc = new ProcessBuilder(explorerPath,url);
                        ApplicationHome home = new ApplicationHome(OpenBrowserUtil.class);
                        /*很有必要 不然会占用target*/
                        File jarPath = home.getDir();
                        File workPath = jarPath;
                        for(int i=0;i<2;i++){//往上级目录
                            if(workPath.getParentFile().exists()){
                                workPath = workPath.getParentFile();
                            }else {
                                break;
                            }
                        }
                        proc.directory(workPath);//非常重要 否则不关闭浏览器 无法maven编译 提示target占用
                        proc.start();
                    }catch (Exception e){
                        e.printStackTrace();
                        openURL(url);
                    }
                    break;
                default:
                    openURL(url);
            }
        }
    }

    public static boolean openURL(String url) {
        try {
            browse(url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void browse(String url) throws Exception {
        //获取操作系统的名字
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            //苹果的打开方式
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
            openURL.invoke(null, new Object[] { url });
        } else if (osName.startsWith("Windows")) {
            //windows的打开方式。
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式
            String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                //执行代码，在brower有值后跳出，
                //这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                //这个值在上面已经成功的得到了一个进程。
                Runtime.getRuntime().exec(new String[] { browser, url });
        }
    }
}
