package com.passManage.us.admin.api.remote;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.passManage.us.admin.common.KeyUtils.Sm4Util;
import com.passManage.us.admin.common.util.DateUtils;
import com.passManage.us.admin.common.util.HttpUtils;
import com.passManage.us.admin.common.util.R;
import com.passManage.us.admin.common.util.ResolveResponUtils;
import com.passManage.us.admin.rmp.model.SysAdminUser;
import com.passManage.us.admin.rmp.service.SysAdminUserService;
import com.passManage.us.core.utils.Md5Util;
import com.passManage.us.model.PpassInstant;
import com.passManage.us.model.PpassOld;
import com.passManage.us.service.ppassinstant.service.PpassInstantService;
import com.passManage.us.service.ppassold.service.PpassOldService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Resource
    private PpassOldService ppassOldService;

    @Resource
    private ResolveResponUtils resolveResponUtils;


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
        String sm4Key= "";
        R ok = R.ok();
        if(perPass.get(0).getPassType().equals("SM2")){
            sm4Key = Sm4Util.encryptEcb(sessionKey, "pubKey:"+perPass.get(0).getPassChildfir()+"\n\npriKey:"+perPass.get(0).getPassChildsec());
            ok.put("keydata",sm4Key);
            ok.put("keytype",Sm4Util.encryptEcb(sessionKey,"SM2"));
            ok.put("keyId",Sm4Util.encryptEcb(sessionKey,perPass.get(0).getPassId()+""));
            return ok;
        }else if(perPass.get(0).getPassType().equals("SM4")){
            sm4Key = Sm4Util.encryptEcb(sessionKey, perPass.get(0).getPassChildfir());
            ok.put("keydata",sm4Key);
            ok.put("keytype",Sm4Util.encryptEcb(sessionKey,"SM4"));
            ok.put("keyId",Sm4Util.encryptEcb(sessionKey,perPass.get(0).getPassId()+""));
            return ok;
        }else if(perPass.get(0).getPassType().equals("AES")){
            sm4Key = Sm4Util.encryptEcb(sessionKey, perPass.get(0).getPassChildfir());
            ok.put("keydata",Sm4Util.encryptEcb(sessionKey,sm4Key));
            ok.put("keytype",Sm4Util.encryptEcb(sessionKey,"AES"));
            ok.put("keyId",Sm4Util.encryptEcb(sessionKey,perPass.get(0).getPassId()+""));
            return ok;
        }else if(perPass.get(0).getPassType().equals("DES")){
            sm4Key = Sm4Util.encryptEcb(sessionKey, perPass.get(0).getPassChildfir());
            ok.put("keydata",sm4Key);
            ok.put("keytype",Sm4Util.encryptEcb(sessionKey,"DES"));
            ok.put("keyId",Sm4Util.encryptEcb(sessionKey,perPass.get(0).getPassId()+""));
            return ok;
        }else if(perPass.get(0).getPassType().equals("RSA")){
            sm4Key = Sm4Util.encryptEcb(sessionKey, "pubKey:"+perPass.get(0).getPassChildfir()+"\n\npriKey:"+perPass.get(0).getPassChildsec());
            ok.put("keydata",sm4Key);
            ok.put("keytype",Sm4Util.encryptEcb(sessionKey,"RSA"));
            ok.put("keyId",Sm4Util.encryptEcb(sessionKey,perPass.get(0).getPassId()+""));
            return ok;
        }else {
            return ok.put("keydata","");
        }

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

    @RequestMapping("changpwd/{username}")
    @ResponseBody
    public R changpwd(HttpServletRequest request, HttpServletResponse response,@RequestBody R r,@PathVariable("username")String username){
        String sessionKey = redisUtils.opsForValue().get(username);
        String encryptData = Sm4Util.decryptEcb(sessionKey, r.get("encryptData")+"");
        Gson gson = new Gson();
        JSONObject jsonObject = gson.fromJson(encryptData, JSONObject.class);
        String oldpasswordMD5 = Md5Util.md5Encode(jsonObject.get("oldpassword").toString().replaceAll("\"", ""));
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", oldpasswordMD5);
        List<SysAdminUser> modelList = sysAdminUserService.getModelList(hashMap);
        if(!CollectionUtils.isEmpty(modelList)){
            SysAdminUser sysAdminUser = modelList.get(0);
            sysAdminUser.setPassword( Md5Util.md5Encode(jsonObject.get("newpassword").toString().replaceAll("\"", "")));
            sysAdminUserService.updateModel(sysAdminUser);
            return new R().put("flag", true);
        } else {
            return new R().put("flag", false);
        }
    }

    @RequestMapping("register")
    @ResponseBody
    public R remoteRegister(@RequestParam("username")String username,@RequestParam("password")String password) throws Exception {
        SysAdminUser sysAdminUser = new SysAdminUser();
        sysAdminUser.setRoleId(2L);
        sysAdminUser.setEnabled(true);
        sysAdminUser.setAccountNonLocked(false);
        sysAdminUser.setAccountNonExpired(false);
        sysAdminUser.setCredentialsNonExpired(false);
        sysAdminUser.setUsername(username);
        sysAdminUser.setPassword(Md5Util.md5Encode(password));
        sysAdminUserService.insertModel(sysAdminUser);
        HttpUtils.get("http://localhost:18087/remote/setUserName/"+username,null);
        return R.ok().put("flag","true");
    }

    @RequestMapping("/deleteKey/{keyId}/{userName}")
    @ResponseBody
    public R deleteKey(@PathVariable("keyId")String keyId, @PathVariable("userName")String userName){
        PpassInstant model = ppassInstantService.getModel(Integer.parseInt(keyId));
        PpassOld ppassOld = new PpassOld();
        BeanUtils.copyProperties(model,ppassOld);
        ppassOldService.insertModel(ppassOld);
        ppassInstantService.deleteModel(Integer.parseInt(keyId));
        return R.ok().put("flag","true");
    }
}
