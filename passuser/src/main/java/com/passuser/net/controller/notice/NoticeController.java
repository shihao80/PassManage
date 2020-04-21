package com.passuser.net.controller.notice;

import com.passuser.net.annotation.AuthToken;
import com.passuser.net.utils.R;
import com.passuser.net.utils.ResolveResponUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller

public class NoticeController {

    @Autowired
    private ResolveResponUtils resolveResponUtils;
    @RequestMapping("notice")
    @AuthToken
    @ResponseBody
    public R getUserNotice(HttpServletRequest request) throws Exception {
        String user = (String)request.getAttribute("user");
        Map<String ,String> noticelist = resolveResponUtils.getGetResponseDecryptData("http://localhost:18088/notice/" + user, null, Arrays.asList("noticelist"));
        return new R().put("noticelist",noticelist.get("noticelist"));
    }
}
