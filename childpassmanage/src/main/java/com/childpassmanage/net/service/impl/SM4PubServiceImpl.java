package com.childpassmanage.net.service.impl;

import com.childpassmanage.net.dao.SM4PubMapper;
import com.childpassmanage.net.dao.UserNameMapper;
import com.childpassmanage.net.pojo.SM4PubPO;
import com.childpassmanage.net.pojo.UserNamePO;
import com.childpassmanage.net.service.SM4PubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SM4PubServiceImpl implements SM4PubService {

    @Autowired
    private SM4PubMapper sm4PubMapper;


    @Override
    public String getSM4PubByUserName(String username) {
        SM4PubPO sm4PubPO = new SM4PubPO();
        sm4PubPO.setUsername(username);
        List<SM4PubPO> select = sm4PubMapper.select(sm4PubPO);
        return select.get(0).getPubkey();
    }

    @Override
    public void insertPubKey(String pubKey, String userName) {
        SM4PubPO sm4PubPO = new SM4PubPO();
        sm4PubPO.setUsername(userName);
        sm4PubPO.setPubkey(pubKey);
        sm4PubMapper.insert(sm4PubPO);
    }
}
