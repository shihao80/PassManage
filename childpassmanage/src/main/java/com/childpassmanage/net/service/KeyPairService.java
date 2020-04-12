package com.childpassmanage.net.service;

import com.childpassmanage.net.pojo.KeyPairPO;
import org.springframework.stereotype.Service;


public interface KeyPairService {

    Integer insertKeyPairByUsername(String username, String priKey);

    KeyPairPO selectKeyPairByUserName(String username);
}
