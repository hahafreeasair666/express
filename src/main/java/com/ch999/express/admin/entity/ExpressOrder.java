package com.ch999.express.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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
@TableName("express_order")
@NoArgsConstructor
public class ExpressOrder extends Model<ExpressOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 发布人id
     */
    @TableField("create_user")
    private Integer createUser;
    /**
     * 送货地址id
     */
    private Integer address;
    /**
     * 取货点信息，有快递点名字，经纬度，取货码
     */
    @TableField("express_info")
    private String expressInfo;
    /**
     * 订单状态，0未接单，1已接单，2订单完成
     */
    @TableField("handle_state")
    private Integer handleState;
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

    @Data
    @NoArgsConstructor
    public static class ExpressInfo{

        @NotNull(message = "快递名称不能为空")
        @NotBlank(message = "快递名称不能为空")
        private String expressName;

        @NotNull(message = "快递点地址信息不能为空")
        @NotBlank(message = "快递点地址信息不能为空")
        private String expressAddress;

        private String expressMobile;

        @JsonIgnore
        private Integer weight;

        @JsonIgnore
        private Double tip;

        private Double price;

        @JsonIgnore
        private String code;

        private String expressMsg;

        @NotNull(message = "快递点坐标不能为空")
        @NotBlank(message = "快递点坐标不能为空")
        @JsonIgnore
        private String position;

        public Double getTip() {
            if(tip == null){
                tip = 0.0;
            }
            return tip;
        }

        public String getCode() {
            if(code == null){
                code = "无";
            }
            return code;
        }

        public String getExpressMsg() {
            if(expressMsg == null){
                expressMsg = "";
            }
            return expressMsg;
        }
    }

    public ExpressOrder(Integer createUser, Integer address) {
        this.createUser = createUser;
        this.address = address;
        this.createTime = new Date();
    }
}
