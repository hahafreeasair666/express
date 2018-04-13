package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * 代取快递者订单详情类
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class ExpressListVO {

    private Integer orderId;

    private String createAvatar;

    private Date createTime;

    private Map<String,Object> employerInfo;

    private Map<String,Object> expressInfo;

    private Double price;

    private Map<String,Object> distanceInfo;
}
