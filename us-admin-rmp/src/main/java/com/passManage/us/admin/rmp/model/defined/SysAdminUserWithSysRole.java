package com.passManage.us.admin.rmp.model.defined;


import com.passManage.us.admin.rmp.model.SysAdminUser;

import java.io.Serializable;

/**
* 代码为自动生成 Created by www.magicalcoder.com
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:323237052
*/
public class SysAdminUserWithSysRole extends SysAdminUser implements Serializable{
        private Long sysRoleId;
    private String roleName;
    public Long getSysRoleId(){
       return sysRoleId;
    }
    public void setSysRoleId(Long sysRoleId){
        this.sysRoleId = sysRoleId;
    }
    public String getRoleName(){
       return roleName;
    }
    public void setRoleName(String roleName){
        this.roleName = roleName;
    }

}
