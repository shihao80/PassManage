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

    @RequestMapping(value = "/remote/ranstr/{length}/{userName}",method = RequestMethod.GET)
    @ResponseBody
    public R getRandomString(@PathVariable("length")String length, @PathVariable("userName") String userName){
        String random = RandomStringUtils.random(Integer.parseInt(length), false, true);
        R r = new R();
        r.put("random", random);
        Boolean flag = randomService.insertRandomStringByUserName(userName, random);
        return flag ? r:R.error();
    }

    @RequestMapping(value = "updateran/{userName}/{keyId}",method = RequestMethod.GET)
    @ResponseBody
    public R updateRandomKeyId(@PathVariable("userName")String userName, @PathVariable("keyId") String keyId){
        Boolean flag = randomService.updateRandomStringByUserNameAndKey(userName, Integer.parseInt(keyId));
        return flag ? R.ok():R.error();
    }
}
