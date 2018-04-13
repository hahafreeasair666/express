package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @since 2018-04-12
 */
@Data
@TableName("detailed_log")
@NoArgsConstructor
public class  DetailedLog extends Model<DetailedLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关联用户表示谁的日志
     */
    @JsonIgnore
    @TableField("user_id")
    private Integer userId;
    /**
     * 日志类型1余额变更2积分变更3信用分变更
     */
    @JsonIgnore
    @TableField("log_type")
    private Integer logType;
    /**
     * 日志内容
     */
    @TableField("log_info")
    private String logInfo;
    /**
     * 删除标志
     */
    @TableField("del_flag")
    @TableLogic
    @JsonIgnore
    private Boolean delFlag;

    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DetailedLog(Integer userId, Integer logType, String logInfo) {
        this.userId = userId;
        this.logType = logType;
        this.logInfo = logInfo;
        this.createTime = new Date();
    }
}
