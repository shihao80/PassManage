package com.passManage.us.admin.common.spring;

import com.passManage.us.core.cache.redis.RedisUtil;
import com.passManage.us.core.common.constant.SystemOperateEnum;
import com.passManage.us.core.utils.OpenBrowserUtil;
import com.passManage.us.core.utils.SystemOperateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by magicalcoder.com on
 * 当spring bean 启动完毕 就会执行此类方法
 */
@Component
@Slf4j
public class SpringBeanAllInitFinished implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private RedisUtil redisUtil;

    /*启动成功是否用浏览器打开管理后台*/
    @Value("${magicalcoder.settings.autoStartExplorer:false}")
    private Boolean autoStartExplorer;
    /*默认浏览器安装路径*/
    @Value("${magicalcoder.settings.explorerPath:}")
    private String explorerPath;

    @Value("${server.servlet.context-path:}")
    private String contextPath;
    @Value("${server.port:80}")
    private Integer port;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent contextRefreshedEvent) {
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
       log.info("spring bean 加载完毕");
       try {
           redisUtil.get("test");
           log.info("------------start success--------------");
       }catch (JedisConnectionException e){
            e.printStackTrace();
            log.error("-------can not connect redis---------");
            log.error("无法连接redis,请确认redis已经启动并且application-xxx.yml中的配置信息正确");
           SystemOperateEnum systemOperateEnum = SystemOperateUtil.system();
           switch (systemOperateEnum){
               case WINDOWS:
                    log.error("如果未启动redis,project目录有redis文件夹，打开后双击redis-启动.bat即可");
                    break;
               case MAC:
               case LINUX:
                    log.error("如果未启动redis,请自行安装");
                    break;
           }
       }
        //使用浏览器协助打开管理后台 请勿使用多线程打开 否则出现浏览器不关闭 占用target文件夹情况
        if(autoStartExplorer){
            String usUrl = String.format("http://localhost:%s%s",port,contextPath);
            log.info("正在使用浏览器打开管理后台地址："+usUrl);
            OpenBrowserUtil.openURL(explorerPath,usUrl);
        }
    }
}
