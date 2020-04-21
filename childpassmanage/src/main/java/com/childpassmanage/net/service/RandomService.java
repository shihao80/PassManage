package com.childpassmanage.net.service;

public interface RandomService {
    public Boolean insertRandomStringByUserName(String userName ,String randomKey,String keyId);
    public Boolean updateRandomStringByUserNameAndKey(String userName, Integer keyId,String randomStr);
    public String getRandomStringByKeyId(String keyId);
}
