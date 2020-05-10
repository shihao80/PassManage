package com.passManage.us.service.ppassinstant.service.impl;

import com.passManage.us.core.utils.CollectionUtil;
import com.passManage.us.core.utils.ListUtil;
import com.passManage.us.model.PpassOld;
import com.passManage.us.service.ppassinstant.mapper.PpassInstantMapper;
import com.passManage.us.service.ppassinstant.service.PpassInstantService;
import com.passManage.us.model.PpassInstant;
import com.passManage.us.core.service.CommonServiceImpl;
import com.passManage.us.core.utils.StringUtil;

import com.passManage.us.service.ppassold.mapper.PpassOldMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.passManage.us.core.utils.CopyUtil;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.*;
import java.util.stream.Collectors;

/**
* 代码为自动生成 Created by www.magicalcoder.com
* 软件作者：何栋宇 qq:709876443
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:648595928
*/

@Service
@Transactional
public class PpassInstantServiceImpl extends CommonServiceImpl<PpassInstant,Integer> implements PpassInstantService,InitializingBean{
    @Resource
    private PpassInstantMapper ppassInstantMapper;

    @Resource
    private PpassOldMapper ppassOldMapper;

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
            PpassOld ppassOld = new PpassOld();
            ppassOld.setPassChildfir(ppassInstant.getPassChildfir());
            ppassOld.setPassChildsec(ppassInstant.getPassChildsec());
            ppassOld.setPassCreatetime(ppassInstant.getPassCreatetime());
            ppassOld.setPassExpiry(ppassInstant.getPassExpiry());
            ppassOld.setPassLength(ppassInstant.getPassLength());
            ppassOld.setPassName(ppassInstant.getPassName());
            ppassOld.setPassUserid(ppassInstant.getPassUserid());
            ppassOld.setPassType(ppassInstant.getPassType());
            ppassOldMapper.insertModel(ppassOld);
        }
        Set<Integer> passOldSet = oldestDatePassKey.stream().map(PpassInstant::getPassId).collect(Collectors.toSet());
        if(!CollectionUtil.isEmpty(passOldSet)){
        count = ppassInstantMapper.deleteModelByIds(passOldSet);}
        return count;
    }
}
