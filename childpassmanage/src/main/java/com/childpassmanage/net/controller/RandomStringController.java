package com.childpassmanage.net.controller;

import com.childpassmanage.net.service.RandomService;
import com.childpassmanage.net.utils.R;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RandomStringController {

    @Autowired
    private RandomService randomService;

    @RequestMapping(value = "remote/ranstr/{length}/{userName}/{keyId}",method = RequestMethod.GET)
    @ResponseBody
    public R getRandomString(@PathVariable("length")String length, @PathVariable("userName") String userName,@PathVariable("keyId")String keyId){
        String random = RandomStringUtils.random(Integer.parseInt(length), false, true);
        R r = new R();
        r.put("random", random);
        Boolean flag = randomService.insertRandomStringByUserName(userName, random,keyId);
        return flag ? r:R.error();
    }


    @RequestMapping(value = "remote/ranstr/{keyId}",method = RequestMethod.GET)
    @ResponseBody
    public R getKeyRanByKeyId(@PathVariable("keyId")String keyId){
        String ranStr = randomService.getRandomStringByKeyId(keyId);
        return R.ok().put("ranstr",ranStr);
    }
}
