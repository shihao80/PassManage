package com.passManage.us.admin.api.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.passManage.us.admin.common.KeyUtils.Sm4Util;
import com.passManage.us.admin.common.util.JedisConnectionUtils;
import com.passManage.us.admin.common.util.R;
import com.passManage.us.admin.rmp.model.SysAdminUser;
import com.passManage.us.admin.rmp.service.SysAdminUserService;
import com.passManage.us.core.utils.Md5Util;
import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/remote")
@Controller
public class RemoteController {

    @Resource
    private SysAdminUserService sysAdminUserService;

    @Resource
    private RedisTemplate<String,String> redisUtils;

    @RequestMapping("/remoteauth")
    @ResponseBody
    public R remoteUserLogin(@RequestParam("userEncrypt") String userEncrypt, @RequestParam("username") String username) {
        String sessionkey = redisUtils.opsForValue().get(username);
        String decryptUser = Sm4Util.decryptEcb(sessionkey, userEncrypt);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(decryptUser, JsonObject.class);
        String password = Md5Util.md5Encode(jsonObject.get("password").toString().replaceAll("\"",""));
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        List<SysAdminUser> modelList = sysAdminUserService.getModelList(hashMap);
        if (CollectionUtils.isEmpty(modelList)) {
            return new R().put("auth", false);
        } else {
            return new R().put("auth", true);
        }
    }

    @RequestMapping("/saveUserSM4KEY")
    public void saveUserSM4KEY(@RequestParam("sessionkey") String sessionkey, @RequestParam("username") String username) {
        redisUtils.opsForValue().set(username, sessionkey);
    }

    @RequestMapping("/getKeyList/{user}")
    public void getKeyList(@PathVariable("user")String user){

    }
}
