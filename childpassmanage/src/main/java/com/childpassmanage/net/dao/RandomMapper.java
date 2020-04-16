package com.childpassmanage.net.dao;

import com.childpassmanage.net.pojo.RandomStringPO;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface RandomMapper extends Mapper<RandomStringPO> {
}
