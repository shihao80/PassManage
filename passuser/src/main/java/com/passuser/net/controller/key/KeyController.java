package com.passuser.net.controller.key;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.passuser.net.KeyUtils.*;
import com.passuser.net.annotation.AuthToken;
import com.passuser.net.entity.PpassInstant;
import com.passuser.net.utils.*;
import lombok.SneakyThrows;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.cookie.SM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private RedisTemplate<String, String> redisTemplate;

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

    @RequestMapping("passInstant_upload")
    public String uploadPpass(){return PREFIX + "passInstant_upload.html";}

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
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", "0");
        hashMap.put("data", nowPassInstant);
        String postJSON = gson.toJson(hashMap);
        return postJSON;
    }

    @RequestMapping("/add")
    @AuthToken
    public String generateKeyAndUpload(PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        String passType = ppassInstant.getPassType();
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        return addNewKeyByData(ppassInstant, request, passType);

    }

    private String addNewKeyByData(PpassInstant ppassInstant, HttpServletRequest request, String passType) throws Exception {
        switch (passType) {
            case "SM4":
                Map<String, String> SM4flag = generateSM4Key(ppassInstant, request);
                SM4flag.put("keyId", ppassInstant.getPassId() + "");
                return SM4flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case "SM2":
                Map<String, String> SM2flag = generateSM2key(ppassInstant, request);
                SM2flag.put("keyId", ppassInstant.getPassId() + "");
                return SM2flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("AES"):
                Map<String, String> AESflag = generateAESKey(ppassInstant, request);
                AESflag.put("keyId", ppassInstant.getPassId() + "");
                return AESflag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("DES"):
                Map<String, String> DESflag = generateDESKey(ppassInstant, request);
                DESflag.put("keyId", ppassInstant.getPassId() + "");
                return DESflag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("RSA"):
                Map<String, String> RSAflag = generateRSAkey(ppassInstant, request);
                RSAflag.put("keyId", ppassInstant.getPassId() + "");
                return RSAflag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
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
    public String updateKeyAndUpload(@PathVariable("keyId") String keyId, @ModelAttribute PpassInstant ppassInstant, Model model, HttpServletRequest request) throws Exception {
        return UpdateKeyAndUpload(keyId, ppassInstant, request);
    }

    private String UpdateKeyAndUpload(@PathVariable("keyId") String keyId, @ModelAttribute PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        String passType = ppassInstant.getPassType();
        switch (passType) {
            case ("SM4"):
                String sm4key = Sm4Util.generateKey();
                Map<String, String> SM4flag = resolveSetPpassInstantById(keyId, ppassInstant, request, sm4key);
                return SM4flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("SM2"):
                Map<String, String> sm2key = getSM2KeyPairToJSON();
                Map<String, String> SM2flag = resolveSetSM2PpassInstantById(keyId, ppassInstant, request, sm2key);
                return SM2flag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("AES"):
                Map<String, String> AESflag = generateAESKey(ppassInstant, request);
                AESflag.put("keyId", ppassInstant.getPassId() + "");
                return AESflag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("DES"):
                Map<String, String> DESflag = generateDESKey(ppassInstant, request);
                DESflag.put("keyId", ppassInstant.getPassId() + "");
                return DESflag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            case ("RSA"):
                Map<String, String> RSAflag = generateRSAkey(ppassInstant, request);
                RSAflag.put("keyId", ppassInstant.getPassId() + "");
                return RSAflag.get("flag").equals("true") ? PREFIX + "passInstant.html" : "404.html";
            default:
                return "404.html";
        }
    }

    @RequestMapping("download/{keyId}")
    @AuthToken
    @ResponseBody
    public String download(@PathVariable("keyId") String keyId, Model model, HttpServletRequest request, HttpServletResponse response,@RequestParam("username")String qianyiUser) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String username = redisTemplate.opsForValue().get(token);
        String key = downloadKeyByKeyId(keyId, username, qianyiUser);
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
        Map<String, String> keyDataMap = getKeyDataMap(keyId, username);
        String keytype = keyDataMap.get("keytype");
        deleteByKeyId(keyId,username);
        PpassInstant ppassInstant = new PpassInstant();
        ppassInstant.setPassType(keytype);
        ppassInstant.setPassId(Integer.parseInt(keyId)+1);
        ppassInstant.setPassName(keyId+" update key");
        ppassInstant.setPassExpiry(DateFormatUtils.format(DateUtils.rollMon(new Date(),1),"yyyy-MM-dd"));
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        return generateKeyAndUpload(ppassInstant,request);
    }

    @RequestMapping("delete/{keyId}")
    @AuthToken
    @ResponseBody
    public String deleteKey(@PathVariable("keyId") String keyId, HttpServletRequest request) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String username = redisTemplate.opsForValue().get(token);
        String flag = deleteByKeyId(keyId, username);
        return flag.equals("true") ? "/passInstant" : "404.html";
    }

    /**
     * 上传推荐资料
     */
    @RequestMapping(value = "/upload",method= RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(
                                file.getOriginalFilename())));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            //上传文件路径
            String path = request.getServletContext().getRealPath("/systemFiles/");
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path, filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            } //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + filename));
            File readTxt = new File(path + File.separator + filename);
            BufferedReader reader = null;
            String tempString = null;
            StringBuffer sb = new StringBuffer();
            try {
                // System.out.println("以行为单位读取文件内容，一次读一整行：");
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(readTxt),"UTF-8"));
                while ((tempString = reader.readLine()) != null) {
                    sb.append(tempString);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String keyStr = sb.toString();
            Gson gson = new Gson();
            PpassInstant ppassInstant = gson.fromJson(keyStr, PpassInstant.class);
            String priKey = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/getSM2Pri/" + ppassInstant.getUsername(), null, "priKey");
            String fir = SM2Util.decrypt(priKey, ppassInstant.getPassChildfir());
            ppassInstant.setPassChildfir(fir);
            if(ppassInstant.getPassChildsec()!=null){
                String sec = SM2Util.decrypt(priKey, ppassInstant.getPassChildsec());
                ppassInstant.setPassChildsec(sec);
                Map<String,String> hashMap = new HashMap<>();
                hashMap.put("priKey",ppassInstant.getPassChildfir());
                hashMap.put("pubKey",ppassInstant.getPassChildsec());
                ppassInstant.setPassId(ppassInstant.getPassId() + 1 );
                Map<String, String> SM2flag = resolveSetPpassInstant(ppassInstant, request, hashMap);
                return SM2flag.get("flag").equals("true") ? "上传成功" : "404.html";
            }
            ppassInstant.setPassId(ppassInstant.getPassId() + 1 );
            Map<String, String> uploadKeyflag = resolveSetPpassInstant(ppassInstant, request,fir);
            return uploadKeyflag.get("flag").equals("true") ? "上传成功" : "404.html";
        } else {
            return "404.html";
        }
    }

    @RequestMapping("/passInstant_download/{ppassId}")
    public String passInstant_download(@PathVariable("ppassId")String ppassId,Model model){
        model.addAttribute("ppassId",ppassId);
        return PREFIX + "passInstant_download.html";
    }

    private String deleteByKeyId(String keyId, String username) throws Exception {
        List<String> flagList = new ArrayList<>();
        flagList.add("flag");
        Map<String, String> flagMap = resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/us-admin/remote/deleteKey/" + keyId + "/" + username, null, flagList);
        String flag = flagMap.get("flag");
        return flag;
    }

    private String updateByKeyId(String keyId, String username) throws Exception {
        List<String> flagList = new ArrayList<>();
        flagList.add("flag");
        Map<String, String> flagMap = resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/us-admin/remote/updateKey/" + keyId + "/" + username, null, flagList);
        String flag = flagMap.get("flag");
        return flag;
    }

    private String downloadKeyByKeyId(String keyId, String username,String qianyiUser) throws Exception {
        String keyPass = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/ranstr/" + username + "/" + keyId, null, "random");
        Map<String, String> getKeyData = getKeyDataMap(keyId, username);
        String keydata = getKeyData.get("keydata");
        if (keydata.contains("pubKey:")) {
            String[] split = keydata.split("pubKey:");
            String[] split1 = split[1].split("priKey:");
            String pubKeyStr = new String(split1[0].getBytes());
            String priKeyStr = new String(split1[1].getBytes());
            String keyPubKey = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/getSM2Pub/" + qianyiUser, null, "pubKey");
            String pubKey = Sm4Util.encryptEcb(keyPass, pubKeyStr);
            String priKey = Sm4Util.encryptEcb(keyPass, priKeyStr);
            String pubKeyEnc = SM2Util.encrypt(keyPubKey, pubKey);
            String priKeyEnc = SM2Util.encrypt(keyPubKey, priKey);
            PpassInstant ppassInstant = new PpassInstant();
            ppassInstant.setPassChildfir(pubKeyEnc);
            ppassInstant.setPassChildsec(priKeyEnc);
            ppassInstant.setPassType(getKeyData.get("keytype"));
            ppassInstant.setUsername(qianyiUser);
            ppassInstant.setPassId(Integer.parseInt(getKeyData.get("keyId")));
            Gson gson = new Gson();
            String keyJson = gson.toJson(ppassInstant);
            return keyJson;
        }
        String keyPubKey = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/getSM2Pub/" + username, null, "pubKey");
        String keyStrs = new String(keydata.getBytes());
        String keyEnc = Sm4Util.encryptEcb(keyPubKey,keyStrs);
        PpassInstant ppassInstant = new PpassInstant();
        ppassInstant.setPassChildfir(keyEnc);
        ppassInstant.setPassType(getKeyData.get("keytype"));
        ppassInstant.setUsername(qianyiUser);
        Gson gson = new Gson();
        String keyJson = gson.toJson(ppassInstant);
        return keyJson;
    }

    private Map<String, String> getKeyDataMap(String keyId, String username) throws Exception {
        List<String> keyStr = new ArrayList<>();
        keyStr.add("keydata");
        keyStr.add("keytype");
        keyStr.add("keyId");
        return resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/us-admin/remote/getKeyById/" + keyId + "/" + username, null, keyStr);
    }

    private Map<String, String> resolveSetSM2PpassInstantById(String keyId, PpassInstant ppassInstant, HttpServletRequest request, Map<String, String> key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName,token);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        return resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keyUploadJson/" + keyId + "/" + userName, postJson, strList);
    }

    private Map<String, String> resolveSetPpassInstantById(String keyId, PpassInstant ppassInstant, HttpServletRequest request, String key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName, token);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        return resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keyUploadJson/" + keyId + "/" + userName, postJson, strList);
    }

    private String getKeyPostBody(PpassInstant ppassInstant, Map<String, String> key, String userName, String token) throws Exception {
        String keyPass = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/ranstr/" + userName + "/" + ppassInstant.getPassId(), null, "random");
        String privateKey = Sm4Util.encryptEcb(keyPass, key.get("priKey"));
        String publicKey = Sm4Util.encryptEcb(keyPass, key.get("pubKey"));
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        ppassInstant.setPassExpiry(DateFormatUtils.format(DateUtils.rollDay(new Date(), 31), "yyyy-MM-dd"));
        ppassInstant.setPassLength(new String(privateKey).length());
        ppassInstant.setPassChildfir(privateKey);
        ppassInstant.setPassChildsec(publicKey);
        Gson gson = new Gson();
        return gson.toJson(ppassInstant);
    }

    private String getKeyPostBody(PpassInstant ppassInstant, String key, String userName, String token) throws Exception {
        String keyPass = resolveResponUtils.getGetResponseData("http://localhost:18087/remote/ranstr/" + userName + "/" + ppassInstant.getPassId(), null, "random");
        String priKeyStr = Sm4Util.encryptEcb(keyPass, key);
        ppassInstant.setPassCreatetime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        ppassInstant.setPassExpiry(DateFormatUtils.format(DateUtils.rollDay(new Date(), 31), "yyyy-MM-dd"));
        ppassInstant.setPassLength(key.length());
        ppassInstant.setPassChildfir(priKeyStr);
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
        String postJson = getKeyPostBody(ppassInstant, key, userName, token);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        strList.add("keyId");
        Map<String, String> postList = resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keySaveJson/" + userName, postJson, strList);
        return postList;
    }

    private Map<String, String> resolveSetPpassInstant(@ModelAttribute PpassInstant ppassInstant, HttpServletRequest request, Map<String, String> key) throws Exception {
        String token = CookieUtils.getCookieValue(request, "Authorization");
        String userName = redisTemplate.opsForValue().get(token);
        String postJson = getKeyPostBody(ppassInstant, key, userName,token);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        strList.add("keyId");
        Map<String, String> postList = resolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/us-admin/remote/keySaveJson/" + userName, postJson, strList);
        return postList;
    }

    private Map<String, String> generateAESKey(PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        String AESkey = Generatingkey.getAESKey();
        return resolveSetPpassInstant(ppassInstant, request, AESkey);
    }

    private Map<String, String> generateDESKey(PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        String DESkey = Generatingkey.getDESKey();
        return resolveSetPpassInstant(ppassInstant, request, DESkey);
    }

    private Map<String, String> getRSAKeyPairToJSON() throws Exception {
        PCIKeyPair pciKeyPair = SM2Util.genKeyPair();
        String priKey = RSAUtils.getPriKey();
        String pubKey = RSAUtils.getPubKey();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("priKey", priKey);
        hashMap.put("pubKey", pubKey);
        return hashMap;
    }

    private Map<String, String> generateRSAkey(PpassInstant ppassInstant, HttpServletRequest request) throws Exception {
        Map<String, String> RSAkey = getRSAKeyPairToJSON();
        return resolveSetPpassInstant(ppassInstant, request, RSAkey);
    }
}
