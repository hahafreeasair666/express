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
@TableName("express_comment")
public class ExpressComment extends Model<ExpressComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 取货单id
     */
    @TableField("express_order_id")
    private Integer expressOrderId;
    /**
     * 评分1到5星
     */
    private Integer star;
    /**
     * 评语
     */
    @TableField("employer_comment")
    private String employerComment;
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

    public Integer getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(Integer expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getEmployerComment() {
        return employerComment;
    }

    public void setEmployerComment(String employerComment) {
        this.employerComment = employerComment;
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
        return "ExpressComment{" +
        "id=" + id +
        ", expressOrderId=" + expressOrderId +
        ", star=" + star +
        ", employerComment=" + employerComment +
        ", delFlag=" + delFlag +
        "}";
    }
}
