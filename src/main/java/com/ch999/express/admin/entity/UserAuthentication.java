package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@TableName("user_authentication")
@NoArgsConstructor
public class UserAuthentication extends Model<UserAuthentication> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户表id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 身份证号
     */
    @TableField("ID_number")
    private String idNumber;
    /**
     * 身份证正反照图片，存image表主键
     */
    @TableField("ID_pic")
    private String idPic;
    /**
     * 删除标志
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdPic() {
        return idPic;
    }

    public void setIdPic(String idPic) {
        this.idPic = idPic;
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
        return "UserAuthentication{" +
        "id=" + id +
        ", userId=" + userId +
        ", idNumber=" + idNumber +
        ", idPic=" + idPic +
        ", delFlag=" + delFlag +
        "}";
    }

    public UserAuthentication(Integer userId, String idNumber, String idPic) {
        this.userId = userId;
        this.idNumber = idNumber;
        this.idPic = idPic;
    }
}
