package com.passManage.us.admin.adminservice.demo.service.impl;

import com.passManage.us.admin.adminservice.demo.mapper.AdminServiceDemoMapper;
import com.passManage.us.admin.adminservice.demo.service.AdminServiceDemoService;
import com.passManage.us.admin.model.MyDemo;
import com.passManage.us.core.utils.MapUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminServiceDemoServiceImpl  implements AdminServiceDemoService {
    @Resource
    private AdminServiceDemoMapper adminServiceDemoMapper;

    @Override
    public MyDemo demoGoods(Long id) {
        return adminServiceDemoMapper.daoDemoGoods(MapUtil.buildMap("id",id));
    }
}
