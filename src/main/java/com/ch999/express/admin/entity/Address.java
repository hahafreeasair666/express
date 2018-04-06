package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.Data;
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
@NoArgsConstructor
public class Address extends Model<Address> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关联用户的id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 地址详情
     */
    @TableField("address_info")
    private String addressInfo;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
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
        return "Address{" +
        "id=" + id +
        ", userId=" + userId +
        ", addressInfo=" + addressInfo +
        ", delFlag=" + delFlag +
        "}";
    }

    public Address(Integer userId, String addressInfo) {
        this.userId = userId;
        this.addressInfo = addressInfo;
    }

    @Data
    @NoArgsConstructor
    public static class AddressInfoVO{

        private String address;

        private String name;

        private String mobile;

        private String position;

        public AddressInfoVO(String address, String name, String mobile, String position) {
            this.address = address;
            this.name = name;
            this.mobile = mobile;
            this.position = position;
        }
    }
}
