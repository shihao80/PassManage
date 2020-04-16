package com.childpassmanage.net.service.impl;

import com.childpassmanage.net.dao.KeyPairMapper;
import com.childpassmanage.net.pojo.KeyPairPO;
import com.childpassmanage.net.service.KeyPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyPairServiceImpl implements KeyPairService {

    @Autowired
    private KeyPairMapper keyPairMapper;

    @Override
    public Integer insertKeyPairByUsername(String username, String priKey) {
        KeyPairPO keyPairPO = new KeyPairPO();
        keyPairPO.setUserName(username);
        keyPairPO.setPubKey(priKey);
        int count = keyPairMapper.insert(keyPairPO);
        return count;
    }

    @Override
    public KeyPairPO selectKeyPairByUserName(String username) {
        KeyPairPO keyPairPO = new KeyPairPO();
        keyPairPO.setUserName(username);
        KeyPairPO keyPairPOS = keyPairMapper.selectOne(keyPairPO);
        return keyPairPOS;
    }
}
