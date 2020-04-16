package com.childpassmanage.net.service.impl;

import com.childpassmanage.net.dao.RandomMapper;
import com.childpassmanage.net.pojo.RandomStringPO;
import com.childpassmanage.net.service.RandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RandomServiceImpl implements RandomService {

    @Autowired
    private RandomMapper randomMapper;

    @Override
    public Boolean insertRandomStringByUserName(String userName, String randomKey) {
        RandomStringPO randomStringPO = new RandomStringPO();
        randomStringPO.setRandomStr(randomKey);
        randomStringPO.setUserName(userName);
        int insert = randomMapper.insert(randomStringPO);
        return insert == 1;
    }

    @Override
    public Boolean updateRandomStringByUserNameAndKey(String userName, Integer keyId) {
        RandomStringPO randomStringPO = new RandomStringPO();
        randomStringPO.setUserName(userName);
        RandomStringPO randomStringPO1 = randomMapper.selectOne(randomStringPO);
        if(randomStringPO1!=null){
            randomStringPO.setKeyID(keyId);
        }
        int update = randomMapper.updateByPrimaryKey(randomStringPO1);
        return update == 1;
    }
}
