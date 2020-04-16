package com.passuser.net.controller.notice;

import com.passuser.net.annotation.AuthToken;
import com.passuser.net.utils.R;
import com.passuser.net.utils.ResolveResponUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller

public class NoticeController {
    @RequestMapping("notice")
    @AuthToken
    public R getUserNotice(HttpServletRequest request) throws Exception {
        String user = (String)request.getAttribute("user");
        String noticelist = ResolveResponUtils.getGetResponseDecryptData("http://localhost:18088/notice/" + user, null, "noticelist");
        return new R().put("noticelist",noticelist);
    }
}
