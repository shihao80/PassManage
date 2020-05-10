package com.passManage.us.service.ppassinstant.mapper;

import com.passManage.us.core.service.ICommonMapper;
import com.passManage.us.model.PpassInstant;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.*;
/**
* 代码为自动生成 Created by www.magicalcoder.com
* 软件作者：何栋宇 qq:709876443
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:648595928
*/

public interface PpassInstantMapper extends ICommonMapper<PpassInstant,Integer>{

    @Update("SELECT * FROM p_pass_instant where pass_expiry < NOW()")
    Integer sendUpdateMsgToUser(@Param("ppassInstat")PpassInstant ppassInstant);

    @Select("SELECT * FROM p_pass_instant where pass_expiry < NOW()")
    @Results({ @Result(id = true, column = "pass_id", property = "passId"),
        @Result(column = "pass_name", property = "passName"),
        @Result(column = "pass_length", property = "passLength") ,
        @Result(column = "pass_type", property = "passType"),
        @Result(column = "pass_childfir", property = "passChildfir") ,
        @Result(column = "pass_childsec", property = "passChildsec") ,
        @Result(column = "pass_expiry", property = "passExpiry") ,
        @Result(column = "pass_createtime", property = "passCreatetime") ,
        @Result(column = "pass_userid", property = "passUserid") ,
    })
    List<PpassInstant> getOldestDatePassKey();

}
