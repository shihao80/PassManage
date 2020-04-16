package com.passuser.net.entity;

import lombok.Data;

/**
 * @Description: Token实体类
 * @author: Yangxf
 * @date: 2019/4/14 12:53
 */
@Data
public class TokenEntity {

    /* 用户ID */
    private Long userName;

    /* 刷新时间 */
    private int buildTime;

    /* token */
    private String token;

}