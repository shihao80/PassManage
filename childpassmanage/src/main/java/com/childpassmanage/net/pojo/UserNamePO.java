package com.childpassmanage.net.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "p_username")
public class UserNamePO {
    private Integer id;
    private String userName;
    private Date createDate;
    private Integer ifClose;
    private String priKey;
}
