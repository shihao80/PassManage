package com.childpassmanage.net.service;

public interface RandomService {
    public Boolean insertRandomStringByUserName(String userName ,String randomKey);
    public Boolean updateRandomStringByUserNameAndKey(String userName, Integer keyId);
}
