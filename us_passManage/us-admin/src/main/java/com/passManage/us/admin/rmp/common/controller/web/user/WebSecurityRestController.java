package com.passManage.us.admin.rmp.common.controller.web.user;

import com.passManage.us.admin.common.util.R;
import com.passManage.us.admin.rmp.common.service.KeyPairService;
import com.passManage.us.admin.rmp.service.SysAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: www.magicalcoder.com
 * date:2018/7/10
 * function:
 */
@RestController
public class WebSecurityRestController {
    @Autowired
    private SysAdminUserService userService;

//    @Autowired
//    private KeyPairService keyPairService;
//    @RequestMapping("/remoteauth")
//    public R remoteAuth(@RequestParam("username")String username, @RequestParam("password")String password){
//        keyPairService
//    }
}
