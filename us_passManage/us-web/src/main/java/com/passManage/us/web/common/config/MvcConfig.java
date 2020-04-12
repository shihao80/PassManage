package com.passManage.us.web.common.config;

import com.passManage.us.core.utils.StringUtil;
import com.passManage.us.web.common.interceptor.WebInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author www.magicalcoder.com
 */
@Component
@Slf4j
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private WebInterceptor webInterceptor;

    /**
     * 拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册监控拦截器
        registry.addInterceptor(webInterceptor).addPathPatterns("/**");
    }

  /*  @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");
    }*/

    //下面的方法是转换日期的 否则日期转换出问题 不支持timestemp
    @Bean
    public Converter<String, Date> stringToDateConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                if(StringUtil.isBlank(source)){
                    return null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(source);
                } catch (Exception e) {
                    log.error("日期转换异常",e);
                }
                return date;
            }
        };
    }

    /**
     *str转timestamp
     * @retun
     */
    @Bean
    public Converter<String, Timestamp> stringToTimeStampConvert() {
        return new Converter<String, Timestamp>() {
            @Override
            public Timestamp convert(String source) {
                if(StringUtil.isBlank(source)){
                    return null;
                }
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp date = null;
                try {
                    date = Timestamp.valueOf(source);
                } catch (Exception e) {
                    log.error("日期转换异常",e);
                }
                return date;
            }
        };
    }



    @Bean
    public Converter<String, Time> stringToTimeConvert() {
        return new Converter<String, Time>() {
            @Override
            public Time convert(String source) {
                if(StringUtil.isBlank(source)){
                    return null;
                }
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Time date = null;
                try {
                    String[] arr = source.trim().split("\\s+");
                    date = Time.valueOf(arr[1]);
                } catch (Exception e) {
                    log.error("日期转换异常",e);
                }
                return date;
            }
        };
    }
}
