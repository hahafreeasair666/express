package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class MyCenterVO  extends  CenterVO {

    private Double myBalance;

    private Integer myIntegral;

    private String myOrder;

    private String myPickUp;

}
