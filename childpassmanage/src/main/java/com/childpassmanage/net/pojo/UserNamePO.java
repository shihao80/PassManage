package com.childpassmanage.net.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UserNamePO {
    private Integer id;
    private String userName;
    private Date createDate;
    private Integer ifClose;
}
