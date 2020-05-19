package com.childpassmanage.net.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "p_passdata")
public class KeyPassPO {
    private Integer id;
    private String username;
    private String keypass;
}
