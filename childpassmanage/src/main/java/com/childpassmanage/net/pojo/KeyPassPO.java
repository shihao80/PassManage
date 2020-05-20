package com.childpassmanage.net.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "p_passdata")
public class KeyPassPO {
    private Integer id;
    private String username;
    private Long dian0;
    private Long dian1;
    private Long dian2;
    private Long f0;
    private Long f1;
    private Long f2;
    private Long p;
}
