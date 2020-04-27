package com.passManage.us.admin.common.schedule;

import com.passManage.us.model.PpassInstant;
import com.passManage.us.service.ppassinstant.service.PpassInstantService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.DateUtils;

import java.util.List;

@Component
@Slf4j
public class passUpdateTask {
    @Autowired
    private PpassInstantService ppassInstantService;
    @Scheduled(cron = "0/10 * * * * ? ") // 间隔5秒执行
    public void passUpdate() {
        List<PpassInstant> oldestDatePassKey = ppassInstantService.getOldestDatePassKey();
        Integer count = ppassInstantService.updateKeyAlertByPassKeyList(oldestDatePassKey);
        log.info(DateUtils.createNow() + "更新提醒成功{}：过期密钥一共"+count+"个");
    }
}
