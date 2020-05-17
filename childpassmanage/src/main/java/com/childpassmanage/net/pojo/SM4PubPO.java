package com.childpassmanage.net.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "p_sm4pub")
public class SM4PubPO {
    private Integer id;
    private String pubkey;
    private String username;
}
