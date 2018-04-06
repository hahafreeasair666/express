package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@Data
@TableName("express_comment")
@NoArgsConstructor
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
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 删除标志
     */
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
