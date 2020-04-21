package com.passManage.us.admin.api.remote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.passManage.us.admin.common.KeyUtils.Sm4Util;
import com.passManage.us.admin.common.util.DateUtils;
import com.passManage.us.admin.common.util.R;
import com.passManage.us.admin.rmp.model.SysAdminUser;
import com.passManage.us.admin.rmp.service.SysAdminUserService;
import com.passManage.us.core.utils.Md5Util;
import com.passManage.us.model.PpassInstant;
import com.passManage.us.service.ppassinstant.service.PpassInstantService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @Resource
    private PpassInstantService ppassInstantService;

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

    @RequestMapping("/getKeyList/{username}")
    @ResponseBody
    public R getKeyList(HttpServletRequest request,@PathVariable("username")String user){
        Map<String ,Object> hashMap = new HashMap<>();
        Map<String ,Object> paramMap = new HashMap<>();
        paramMap.put("username",user);
        List<SysAdminUser> modelList = sysAdminUserService.getModelList(paramMap);
        hashMap.put("passUserid",modelList.get(0).getId());
        List<PpassInstant> ppassInstantList = ppassInstantService.getModelList(hashMap);
        String sessionKey = redisUtils.opsForValue().get(user);
        Gson gson =new Gson();
        String ppassInstant = gson.toJson(ppassInstantList);
        String sm4Post = Sm4Util.encryptEcb(sessionKey, ppassInstant);
        return R.ok().put("encryptData", sm4Post);
    }

    @RequestMapping("/keySaveJson/{userName}")
    @ResponseBody
    public R uploadUserKey(@RequestBody PpassInstant ppassInstant,@PathVariable("userName")String userName){
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("username",userName);
        List<SysAdminUser> modelList = sysAdminUserService.getModelList(hashMap);
        ppassInstant.setPassUserid(Integer.parseInt(modelList.get(0).getId()+""));
        int i = ppassInstantService.insertModel(ppassInstant);
        boolean flag = i == 1;
        String sessionkey = redisUtils.opsForValue().get(userName);
        String flagStr = Sm4Util.encryptEcb(sessionkey, flag + "");
        return R.ok().put("flag",flagStr).put("keyId",ppassInstant.getPassId());
    }

    @RequestMapping("/getKeyById/{keyId}/{username}")
    @ResponseBody
    public R returnKeyById(@PathVariable("keyId")String keyId,@PathVariable("username")String username){
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("passId",keyId);
        List<PpassInstant> perPass = ppassInstantService.getModelList(hashMap);
        String sessionKey = redisUtils.opsForValue().get(username);
        String sm4Key = Sm4Util.encryptEcb(sessionKey, perPass.get(0).getPassChildfir());
        return R.ok().put("keydata",sm4Key);
    }

    @RequestMapping(value = "/updateKey/{keyId}/{username}",method = RequestMethod.GET)
    @ResponseBody
    public R updateKeyString(@PathVariable("keyId")String keyId,@PathVariable("username")String username){
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("passId",keyId);
        PpassInstant ppassInstant = ppassInstantService.getModelList(hashMap).get(0);
        Date passExpiry = ppassInstant.getPassExpiry();
        ppassInstant.setPassExpiry(DateUtils.rollDay(passExpiry,30));
        int count = ppassInstantService.updateModel(ppassInstant);
        Boolean flag = false;
        String sessionkey = redisUtils.opsForValue().get(username);
        if(count==1){
            flag =true;
        }else{
            flag =false;
        }
        String flagdata = Sm4Util.encryptEcb(sessionkey, flag + "");
        return R.ok().put("flag",flagdata);
    }

}
