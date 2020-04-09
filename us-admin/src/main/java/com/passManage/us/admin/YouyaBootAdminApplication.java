package com.passManage.us.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.passManage.us.core",
        "com.passManage.us.service",
        "com.passManage.us.admin"
        })
/*让过滤器WebFilter生效*/
@ServletComponentScan(basePackages = {
    "com.passManage.us.admin.common.filter",
    "com.passManage.us.admin.rmp.common.filter"
})
public class YouyaBootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouyaBootAdminApplication.class, args);
    }

}
