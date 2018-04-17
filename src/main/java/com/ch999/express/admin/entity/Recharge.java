package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2018-04-17
 */
public class Recharge extends Model<Recharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 密钥
     */
    private String code;
    /**
     * 面值
     */
    private Double price;
    /**
     * 已使用标志
     */
    @TableField("used_flag")
    private Boolean usedFlag;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getUsedFlag() {
        return usedFlag;
    }

    public void setUsedFlag(Boolean usedFlag) {
        this.usedFlag = usedFlag;
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
        return "Recharge{" +
        "id=" + id +
        ", code=" + code +
        ", price=" + price +
        ", usedFlag=" + usedFlag +
        ", delFlag=" + delFlag +
        "}";
    }

    public Recharge(String code) {
        this.code = code;
    }
}
