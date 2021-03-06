package com.passManage.us.admin.rmp.service;

import com.passManage.us.admin.rmp.model.SysModule;
import com.passManage.us.core.service.ICommonService;

/**
* 代码为自动生成 Created by www.magicalcoder.com
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:323237052
*/

public interface SysModuleService extends ICommonService<SysModule,Long>{

    /**
     * 保存模块与权限
     * @param helpConfigPermit 帮助配置权限
     * @param entity 模块实体类
     */
    void saveModule(Boolean helpConfigPermit,SysModule entity);

}
