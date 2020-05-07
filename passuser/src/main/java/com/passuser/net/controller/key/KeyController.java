package com.passuser.net.controller.key;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.passuser.net.KeyUtils.*;
import com.passuser.net.annotation.AuthToken;
import com.passuser.net.entity.PpassInstant;
import com.passuser.net.utils.CookieUtils;
import com.passuser.net.utils.DateUtils;
import com.passuser.net.utils.R;
import com.passuser.net.utils.ResolveResponUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/passInstant")
public class KeyController {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ResolveResponUtils resolveResponUtils;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String PREFIX = "/PassInstant/passInstant/";

    /**
     * 跳转到密钥管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "passInstant.html";
    }

    /**
     * 跳转到添加密钥管理
     */
    @RequestMapping("/passInstant_add")
    public String passInstantAdd() {
        return PREFIX + "passInstant_add.html";
    }

    @RequestMapping("/list")
    @AuthToken
    @ResponseBody
    public String getKeyList(HttpServletRequest request) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String username = redisTemplate.opsForValue().get(token);
        List<String> strList = new ArrayList<>();
        strList.add("encryptData");
        Map<String, String> encryptData = resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/us-admin/remote/getKeyList/" + username, null, strList);
        Gson gson = new Gson();
        List<PpassInstant> nowPassInstant = gson.fromJson(encryptData.get("encryptData"), new TypeToken<List<PpassInstant>>() {
        }.getType());
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("code","0");
        hashMap.put("data",nowPassInstant);
        String postJSON = gson.toJson(hashMap);
        return postJSON;
    }

    @RequestMapping("/add")
    @AuthToken
    public String generateKeyAndUpload(PpassInstant ppassInstant,HttpServletRequest request) throws Exception {
        String passType = ppassInstant.getPassType();
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
        switch (passType) {
            case "SM4":
                Map<String, String> SM4flag = generateSM4Key(ppassInstant, request);
                SM4flag.put("keyId",ppassInstant.getPassId()+"");
                return SM4flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case "SM2":
                Map<String, String> SM2flag = generateSM2key(ppassInstant, request);
                SM2flag.put("keyId",ppassInstant.getPassId()+"");
                return SM2flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            default:
                return "404.html";
        }

    }

    private Map<String, String> generateSM2key(PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        Map<String, String> sm2key = getSM2KeyPairToJSON();
        return resolveSetPpassInstant(ppassInstant, request, sm2key);
    }

    private Map<String, String> generateSM4Key(PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        String sm4key = Sm4Util.generateKey();
        return resolveSetPpassInstant(ppassInstant, request, sm4key);
    }


    @RequestMapping("updateKey/{keyId}")
    @AuthToken
    public String updateKeyAndUpload(@PathVariable("keyId") String keyId, @ModelAttribute PpassInstant ppassInstant, Model model,HttpServletRequest request) throws Exception {
        String passType = ppassInstant.getPassType();
        switch (passType) {
            case ("SM4"):
                String sm4key = Sm4Util.generateKey();
                Map<String, String> SM4flag = resolveSetPpassInstantById(keyId, ppassInstant, request, sm4key);
                return SM4flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("SM2"):
                Map<String,String> sm2key = getSM2KeyPairToJSON();
                Map<String, String> SM2flag = resolveSetSM2PpassInstantById(keyId, ppassInstant, request, sm2key);
                return SM2flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            default:
                return "404.html";
        }
    }

    @RequestMapping("download/{keyId}")
    @AuthToken
    @ResponseBody
    public String download(@PathVariable("keyId") String keyId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String username = redisTemplate.opsForValue().get(token);
        String key = downloadKeyByKeyId(keyId,username);
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new ByteArrayInputStream(key.getBytes("utf-8")));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + new String("key.txt".getBytes()));
        response.addHeader("Content-Length", "" + key.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        return "下载成功";
    }

    @RequestMapping("update/{keyId}")
    @AuthToken
    @ResponseBody
    public String updateKey(@PathVariable("keyId") String keyId, HttpServletRequest request) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String username = redisTemplate.opsForValue().get(token);
        String flag = updateByKeyId(keyId,username);
        return flag.equals("true")?"/passInstant":"404.html";
    }

    private String updateByKeyId(String keyId, String username) throws Exception {
        List<String> flagList = new ArrayList<>();
        flagList.add("flag");
        Map<String, String> flagMap = resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/us-admin/remote/updateKey/" + keyId + "/" + username, null, flagList);
        String flag = flagMap.get("flag");
        return flag;
    }

    private String downloadKeyByKeyId(String keyId,String username) throws Exception {
        List<String> keyStr = new ArrayList<>();
        keyStr.add("keydata");
        Map<String, String> getKeyData = resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/us-admin/remote/getKeyById/" + keyId +"/" + username, null, keyStr);
        String keydata = getKeyData.get("keydata");
        String ranstr = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/ranstr/" + keyId, null, "ranstr");
        if(keydata.contains("pubKey:")){
            String[] split = keydata.split("pubKey:");
            String[] split1 = split[1].split("priKey:");
            byte[] pubKey = XORUtils.encrypt(split1[0].getBytes(), ranstr.getBytes());
            byte[] priKey = XORUtils.encrypt(split1[1].getBytes(), ranstr.getBytes());
            String pubKeyStr = new String(pubKey);
            String priKeyStr = new String(priKey);
            return "pubKey:"+pubKeyStr+"\n\n"+"priKeyStr:"+priKeyStr;
        }
        byte[] keyOrigin = XORUtils.encrypt(keydata.getBytes(), ranstr.getBytes());
        String keyStrs = new String(keyOrigin);
        return keyStrs;
    }

    private Map<String, String> resolveSetSM2PpassInstantById(String keyId, PpassInstant ppassInstant, HttpServletRequest request, Map<String, String> key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        return resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keyUploadJson/" + keyId + "/" + userName, postJson, strList);
    }

    private Map<String, String> resolveSetPpassInstantById(String keyId, PpassInstant ppassInstant, HttpServletRequest request, String key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        return resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keyUploadJson/" + keyId + "/" + userName, postJson, strList);
    }

    private String getKeyPostBody(PpassInstant ppassInstant, Map<String, String> key, String userName) throws Exception {
        String random = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/ranstr/"+key.get("pubKey").length()+"/" + userName +"/"+ppassInstant.getPassId(), null, "random");
        byte[] privateKey = XORUtils.encrypt(key.get("pubKey").getBytes(), random.getBytes());
        byte[] publicKey = XORUtils.encrypt(key.get("priKey").getBytes(), random.getBytes());
        String privateKeyStr = new String(privateKey);
        String publicKeyStr = new String(publicKey);
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
        ppassInstant.setPassExpiry(DateFormatUtils.format(DateUtils.rollDay(new Date(), 31),"yyyy-MM-dd"));
        ppassInstant.setPassLength(privateKeyStr.length());
        ppassInstant.setPassChildfir(privateKeyStr);
        ppassInstant.setPassChildsec(publicKeyStr);
        Gson gson = new Gson();
        return gson.toJson(ppassInstant);
    }

    private String getKeyPostBody(PpassInstant ppassInstant, String key, String userName) throws Exception {
        String random = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/ranstr/"+key.length()+"/" + userName +"/"+ppassInstant.getPassId(), null, "random");
        byte[] privateKey = XORUtils.encrypt(key.getBytes(), random.getBytes());
        String privateKeyStr = new String(privateKey);
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
        ppassInstant.setPassExpiry(DateFormatUtils.format(DateUtils.rollDay(new Date(), 31),"yyyy-MM-dd"));
        ppassInstant.setPassLength(privateKeyStr.length());
        ppassInstant.setPassChildfir(privateKeyStr);
        Gson gson = new Gson();
        return gson.toJson(ppassInstant);
    }

    private Map<String, String> getSM2KeyPairToJSON() {
        PCIKeyPair pciKeyPair = SM2Util.genKeyPair();
        String priKey = pciKeyPair.getPriKey();
        String pubKey = pciKeyPair.getPubKey();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("priKey", priKey);
        hashMap.put("pubKey", pubKey);
        return hashMap;
    }

    private Map<String, String> resolveSetPpassInstant(@ModelAttribute PpassInstant ppassInstant, HttpServletRequest request, String key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        strList.add("keyId");
        Map<String, String> postList = resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keySaveJson/" + userName, postJson, strList);
        return postList;
    }

    private Map<String, String> resolveSetPpassInstant(@ModelAttribute PpassInstant ppassInstant, HttpServletRequest request, Map<String, String> key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        strList.add("keyId");
        Map<String, String> postList = resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keySaveJson/" + userName, postJson, strList);
        return postList;
    }
}
