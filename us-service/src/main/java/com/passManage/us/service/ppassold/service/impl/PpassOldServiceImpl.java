package com.passManage.us.service.ppassold.service.impl;

import com.passManage.us.service.ppassold.mapper.PpassOldMapper;
import com.passManage.us.service.ppassold.service.PpassOldService;
import com.passManage.us.model.PpassOld;
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
public class PpassOldServiceImpl extends CommonServiceImpl<PpassOld,Integer> implements PpassOldService,InitializingBean{
    @Resource
    private PpassOldMapper ppassOldMapper;

    @Override
    public void afterPropertiesSet() {
        super.commonMapper = ppassOldMapper;
    }
}
