package com.childpassmanage.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.childpassmanage.net.dao")
public class ChildpassmanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChildpassmanageApplication.class, args);
    }

}
