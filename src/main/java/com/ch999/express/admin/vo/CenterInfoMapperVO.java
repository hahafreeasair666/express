package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class CenterInfoMapperVO {

    /**
     * userId
     */
    private Integer userId;

    /**
     * 未支付
     */
    private Integer notPay;

    /**
     * 未接单
     */
    private Integer noOrder;

    /**
     * 在途
     */
    private Integer transport;

    /**
     * 未评价
     */
    private Integer notComment;

    /**
     * 已完成
     */
    private Integer complete1;

    /**
     * 未送达
     */
    private Integer noComplete;

    /**
     * 已送达
     */
    private Integer complete2;


}
