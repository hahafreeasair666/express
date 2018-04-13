package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class UserOrderVO {

    private Integer orderId;

    private String expressPoint;

    private Integer state;


}
