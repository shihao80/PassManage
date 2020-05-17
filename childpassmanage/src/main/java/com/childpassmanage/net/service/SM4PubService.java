package com.childpassmanage.net.service;

public interface SM4PubService {
    String getSM4PubByUserName(String username);

    void insertPubKey(String pubKey, String userName);
}
