package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class ExpressListVO {

    private Integer orderId;

    private Map<String,Object> employerInfo;

    private Map<String,Object> expressInfo;

    private Double price;

    private Map<String,Object> distanceInfo;
}
