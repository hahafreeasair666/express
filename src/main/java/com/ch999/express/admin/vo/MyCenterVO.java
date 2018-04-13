package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class MyCenterVO  extends  CenterVO {

    private Double myBalance;

    private Integer myIntegral;

    private Map<String,Object> myOrder;

    private Map<String,Object> myPickUp;

}
