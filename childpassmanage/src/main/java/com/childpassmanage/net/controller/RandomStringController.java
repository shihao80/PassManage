package com.childpassmanage.net.controller;

import com.childpassmanage.net.dao.KeyPassMapper;
import com.childpassmanage.net.pojo.KeyPassPO;
import com.childpassmanage.net.service.RandomService;
import com.childpassmanage.net.utils.AESUtil;
import com.childpassmanage.net.utils.R;
import com.childpassmanage.net.utils.Sm4Util;
import com.childpassmanage.net.utils.ThroughUserkey;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RandomStringController {

    @Autowired
    private RandomService randomService;

    @Autowired
    private KeyPassMapper keyPassMapper;

    @RequestMapping(value = "remote/ranstr/{userName}/{keyId}",method = RequestMethod.GET)
    @ResponseBody
    public R getRandomString(@PathVariable("userName") String userName,@PathVariable("keyId")String keyId) throws Exception {
        String sm4Key = Sm4Util.generateKey();
        R r = new R();
        r.put("random", sm4Key);
        KeyPassPO keyPassPO = new KeyPassPO();
        keyPassPO.setUsername(userName);
        List<KeyPassPO> select = keyPassMapper.select(keyPassPO);
        String keypass = select.get(0).getKeypass();
        String keyByPass = ThroughUserkey.getKeyByPass(keypass);
        byte[] encryptSM4key = AESUtil.encrypt(sm4Key, keyByPass);
        Boolean flag = randomService.insertRandomStringByUserName(userName,new String(encryptSM4key),keyId);
        return flag ? r:R.error();
    }


    @RequestMapping(value = "remote/ranstr/{keyId}",method = RequestMethod.GET)
    @ResponseBody
    public R getKeyRanByKeyId(@PathVariable("keyId")String keyId){
        String ranStr = randomService.getRandomStringByKeyId(keyId);
        return R.ok().put("ranstr",ranStr);
    }
}
