package com.childpassmanage.net.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "p_random")
public class RandomStringPO {
    private Integer id;
    private String userName;
    private String randomStr;
    private Integer keyId;

}
