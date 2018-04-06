package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
@NoArgsConstructor
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID = 1L;

    public static final String CK = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[6])|(18[0,5-9])|(19[9]))\\d{8}$";

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
    private String studentNumber;
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
    private String mobile;
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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getAvatar() {
        if(StringUtils.isBlank(avatar)){
            avatar = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522751654036&di=7af9f2e8404b3bb6834e5f4dbd349e19&imgtype=0&src=http%3A%2F%2Fim5.tongbu.com%2FArticleImage%2F2fb7c966-1.jpg%3Fw%3D480%2C343";
        }
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
                ", mobil=" + mobile +
                ", authenticationFlag=" + authenticationFlag +
                ", delFlag=" + delFlag +
                "}";
    }

    public UserInfo(String userName, String pwd, String mobile) {
        this.userName = userName;
        this.pwd = pwd;
        this.mobile = mobile;
    }
}
