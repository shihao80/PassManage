package com.passuser.net.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 代码为自动生成 Created by www.magicalcoder.com
* 软件作者：何栋宇 qq:709876443
* 如果你改变了此类 read 请将此行删除
* 欢迎加入官方QQ群:648595928
*/
@Data
public class PpassInstant implements Serializable{

    private Integer passId;//密钥UUID
    private String passName;//密钥名称
    private Integer passLength;//密钥长度
    private String passType;//密钥类型
    private String passChildfir;//第一子密钥
    private String passChildsec;//第二子密钥
    private String passChildthi;//第三子密钥
    private String passExpiry;//密钥有效期
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String passCreatetime;//密钥上传时间
    private Integer passUserid;//密钥使用者
    private String username;
    
    public Integer getPassId(){
        return passId;
    }
    public void setPassId(Integer passId){
        this.passId = passId;
    }

    public String getPassName(){
        return passName;
    }
    public void setPassName(String passName){
        this.passName = passName;
    }

    public Integer getPassLength(){
        return passLength;
    }
    public void setPassLength(Integer passLength){
        this.passLength = passLength;
    }

    public String getPassType(){
        return passType;
    }
    public void setPassType(String passType){
        this.passType = passType;
    }

    public String getPassChildfir(){
        return passChildfir;
    }
    public void setPassChildfir(String passChildfir){
        this.passChildfir = passChildfir;
    }

    public String getPassChildsec(){
        return passChildsec;
    }
    public void setPassChildsec(String passChildsec){
        this.passChildsec = passChildsec;
    }

    public String getPassChildthi(){
        return passChildthi;
    }
    public void setPassChildthi(String passChildthi){
        this.passChildthi = passChildthi;
    }

    public String getPassExpiry(){
        return passExpiry;
    }
    public void setPassExpiry(String passExpiry){
        this.passExpiry = passExpiry;
    }

    public String getPassCreatetime(){
        return passCreatetime;
    }
    public void setPassCreatetime(String passCreatetime){
        this.passCreatetime = passCreatetime;
    }

    public Integer getPassUserid(){
        return passUserid;
    }
    public void setPassUserid(Integer passUserid){
        this.passUserid = passUserid;
    }
}
