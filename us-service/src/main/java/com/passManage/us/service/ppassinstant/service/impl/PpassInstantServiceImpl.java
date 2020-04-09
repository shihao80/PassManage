package com.passManage.us.service.ppassinstant.service.impl;

import com.passManage.us.service.ppassinstant.mapper.PpassInstantMapper;
import com.passManage.us.service.ppassinstant.service.PpassInstantService;
import com.passManage.us.model.PpassInstant;
import com.passManage.us.core.service.CommonServiceImpl;
import com.passManage.us.core.utils.StringUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.passManage.us.core.utils.CopyUtil;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.*;
/**
* 代码为自动生成 Created by www.magicalcoder.com
* 软件作者：何栋宇 qq:709876443
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:648595928
*/

@Service
public class PpassInstantServiceImpl extends CommonServiceImpl<PpassInstant,Integer> implements PpassInstantService,InitializingBean{
    @Resource
    private PpassInstantMapper ppassInstantMapper;

    @Override
    public void afterPropertiesSet() {
        super.commonMapper = ppassInstantMapper;
    }

    @Override
    public String updateKeyAlert(String id) {
        PpassInstant model = this.getModel(Integer.parseInt(id));
        Integer count = ppassInstantMapper.sendUpdateMsgToUser(model);
        String msg = "";
        if(count == 1){
            msg = "提醒更新密钥成功";
        }else{
            msg = "提醒更新密钥失败";
        }
        return msg;
    }

    @Override
    public List<PpassInstant> getOldestDatePassKey() {
        List<PpassInstant> oldPpassList= ppassInstantMapper.getOldestDatePassKey();
        return oldPpassList;
    }

    @Override
    public Integer updateKeyAlertByPassKeyList(List<PpassInstant> oldestDatePassKey) {
        Integer count = 0;
        for (PpassInstant ppassInstant : oldestDatePassKey) {
            count += ppassInstantMapper.sendUpdateMsgToUser(ppassInstant);
        }
        return count;
    }
}
