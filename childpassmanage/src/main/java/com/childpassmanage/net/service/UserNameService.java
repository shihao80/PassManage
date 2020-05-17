package com.childpassmanage.net.service;

import com.childpassmanage.net.pojo.UserNamePO;

public interface UserNameService {
    /**
     * 查找用户名是否存在
     * @param username
     * @return
     */
    boolean findUserNameIfExist(String username);

    /**
     * 插入新用户名
     * @param userNamePO
     */
    void insertUserName(UserNamePO userNamePO);

    String getSM4PriByUserName(String username);
}
