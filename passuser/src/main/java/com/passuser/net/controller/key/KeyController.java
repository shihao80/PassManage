package com.passuser.net.controller.key;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.passuser.net.KeyUtils.*;
import com.passuser.net.annotation.AuthToken;
import com.passuser.net.entity.PpassInstant;
import com.passuser.net.utils.DateUtils;
import com.passuser.net.utils.R;
import com.passuser.net.utils.ResolveResponUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("key")
public class KeyController {
    @RequestMapping("keylist")
    @AuthToken
    public R getKeyList(HttpServletRequest request) throws Exception {
        String user = (String) request.getAttribute("user");
        List<String> strList = new ArrayList<>();
        strList.add("encryptData");
        Map<String, String> encryptData = ResolveResponUtils.getGetResponseDecryptData("http://localhost:18088/getKeyList/" + user, null, strList);
        Gson gson = new Gson();
        List<PpassInstant> nowPassInstant = gson.fromJson(encryptData.get("encryptData"), new TypeToken<List<PpassInstant>>() {
        }.getType());
        nowPassInstant.sort(new Comparator<PpassInstant>() {
            @Override
            public int compare(PpassInstant o1, PpassInstant o2) {
                if (o1.getPassExpiry().getTime() > o2.getPassExpiry().getTime()) {
                    return 1;
                } else if (o1.getPassExpiry().getTime() < o2.getPassExpiry().getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return new R().put("passlist", nowPassInstant);
    }

    @RequestMapping("generateKey")
    @AuthToken
    public R generateKeyAndUpload(@ModelAttribute PpassInstant ppassInstant, Model model) throws Exception {
        String passType = ppassInstant.getPassType();
        String user =(String) model.getAttribute("user");
        switch (passType) {
            case ("SM4"):
                String sm4key = Sm4Util.generateKey();
                Map<String, String> SM4flag = resolveSetPpassInstant(ppassInstant, model, sm4key);
                updateRandomStrKeyID(user, SM4flag);
                return SM4flag.get("flag").equals("true") ? R.ok() : R.error();
            case ("SM3"):
                String sm3key = Sm3Utils.generateKey();
                Map<String, String> SM3flag = resolveSetPpassInstant(ppassInstant, model, sm3key);
                updateRandomStrKeyID(user, SM3flag);
                return SM3flag.get("flag").equals("true") ? R.ok() : R.error();
            case ("SM2"):
                String sm2key = getSM2KeyPairToJSON();
                Map<String, String> SM2flag = resolveSetPpassInstant(ppassInstant, model, sm2key);
                updateRandomStrKeyID(user, SM2flag);
                return SM2flag.get("flag").equals("true") ? R.ok() : R.error();
            default:
                return R.error("秘钥类型错误");
        }

    }

    private void updateRandomStrKeyID(String user, Map<String, String> SM4flag) {
        String keyId = SM4flag.get("keyId");
        ResolveResponUtils.resolveGetRespons("http://localhost:18087/updateran/"+user+"/"+keyId,null);
    }

    @RequestMapping("updateKey/{keyId}")
    public R updateKeyAndUpload(@PathVariable("keyId") String keyId, @ModelAttribute PpassInstant ppassInstant, Model model) throws Exception {
        String passType = ppassInstant.getPassType();
        switch (passType) {
            case ("SM4"):
                String sm4key = Sm4Util.generateKey();
                Map<String, String> SM4flag = resolveSetPpassInstantById(keyId, ppassInstant, model, sm4key);
                return SM4flag.get("flag").equals("true") ? R.ok() : R.error();
            case ("SM3"):
                String sm3key = Sm3Utils.generateKey();
                Map<String, String> SM3flag = resolveSetPpassInstantById(keyId, ppassInstant, model, sm3key);
                return SM3flag.get("flag").equals("true") ? R.ok() : R.error();
            case ("SM2"):
                String sm2key = getSM2KeyPairToJSON();
                Map<String, String> SM2flag = resolveSetPpassInstantById(keyId, ppassInstant, model, sm2key);
                return SM2flag.get("flag").equals("true") ? R.ok() : R.error();
            default:
                return R.error("秘钥类型错误");
        }
    }

    private Map<String, String> resolveSetPpassInstantById(String keyId, PpassInstant ppassInstant, Model model, String key) throws Exception {
        String userName = (String) model.getAttribute("user");
        String postJson = getKeyPostBody(ppassInstant, key, userName);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        return ResolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/keyUploadJson/" + keyId + "/" + userName, postJson, strList);
    }

    private String getKeyPostBody(PpassInstant ppassInstant, String key, String userName) throws Exception {
        String random = ResolveResponUtils.getGetResponseData("http://localhsot:18087/ranstr/128/" + userName, null, "random");
        byte[] encryptKey = XORUtils.encrypt(key.getBytes(), random.getBytes());
        String keyStr = ByteArrayUtil.toHexString(encryptKey);
        ppassInstant.setPassCreatetime(new Date());
        ppassInstant.setPassExpiry(DateUtils.rollDay(new Date(), 31));
        ppassInstant.setPassLength(keyStr.length());
        Gson gson = new Gson();
        return gson.toJson(ppassInstant);
    }

    private String getSM2KeyPairToJSON() {
        PCIKeyPair pciKeyPair = SM2Util.genKeyPair();
        String priKey = pciKeyPair.getPriKey();
        String pubKey = pciKeyPair.getPubKey();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("priKey", priKey);
        hashMap.put("pubKey", pubKey);
        Gson gson = new Gson();
        return gson.toJson(hashMap);
    }

    private Map<String, String> resolveSetPpassInstant(@ModelAttribute PpassInstant ppassInstant, Model model, String key) throws Exception {
        String userName = (String) model.getAttribute("user");
        String postJson = getKeyPostBody(ppassInstant, key, userName);
        List<String> strList = new ArrayList<>();
        strList.add("flag");
        strList.add("keyId");
        Map<String, String> postList = ResolveResponUtils.getPostJsonResponseDecryptData("http://localhost:18088/keySaveJson/" + userName, postJson, strList);
        return postList;
    }
}
