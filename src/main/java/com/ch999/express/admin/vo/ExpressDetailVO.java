package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class ExpressDetailVO {

    private Integer orderId;

    private Map<String,Object> addressInfo;

    private Map<String,Object> expressInfo;

    private Map<String,Object> employeeInfo;

    private Boolean isCanConfirm = false;
}
