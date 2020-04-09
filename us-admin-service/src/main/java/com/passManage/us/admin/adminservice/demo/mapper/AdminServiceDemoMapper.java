package com.passManage.us.admin.adminservice.demo.mapper;

import com.passManage.us.admin.model.MyDemo;
import com.passManage.us.model.Goods;

import java.util.Map;

public interface AdminServiceDemoMapper {
    MyDemo daoDemoGoods(Map<String,Object> query);

}
