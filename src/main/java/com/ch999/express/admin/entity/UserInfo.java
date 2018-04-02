package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@TableName("user_info")
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 昵称，用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户真实姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 学号
     */
    @TableField("student_number")
    private Integer studentNumber;
    /**
     * 头像路径
     */
    private String avatar;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 手机号码
     */
    private String mobil;
    /**
     * 是否认证
     */
    @TableField("authentication_flag")
    private Boolean authenticationFlag;
    /**
     * 是否删除
     */
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public Boolean getAuthenticationFlag() {
        return authenticationFlag;
    }

    public void setAuthenticationFlag(Boolean authenticationFlag) {
        this.authenticationFlag = authenticationFlag;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
        "id=" + id +
        ", userName=" + userName +
        ", realName=" + realName +
        ", studentNumber=" + studentNumber +
        ", avatar=" + avatar +
        ", pwd=" + pwd +
        ", mobil=" + mobil +
        ", authenticationFlag=" + authenticationFlag +
        ", delFlag=" + delFlag +
        "}";
    }
}
