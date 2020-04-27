package com.childpassmanage.net.service.impl;

import com.childpassmanage.net.dao.UserNameMapper;
import com.childpassmanage.net.pojo.UserNamePO;
import com.childpassmanage.net.service.UserNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserNameServiceImpl implements UserNameService {
    @Autowired
    private UserNameMapper userMapper;

    @Override
    public boolean findUserNameIfExist(String username) {
        UserNamePO userNamePO = new UserNamePO();
        userNamePO.setUserName(username);
        UserNamePO userNamePO1 = userMapper.selectOne(userNamePO);
        if(userNamePO1 != null || userNamePO1.getIfClose() != -1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void insertUserName(UserNamePO userNamePO) {
        userMapper.insert(userNamePO);
    }
}
