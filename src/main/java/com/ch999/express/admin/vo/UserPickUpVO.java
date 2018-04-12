package com.ch999.express.admin.vo;

import com.alibaba.fastjson.JSONObject;
import com.ch999.express.admin.entity.ExpressOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class UserPickUpVO {

    private Integer orderId;

    @JsonIgnore
    private Integer userId;

    private String completeFlag;

    @JsonIgnore
    private String expressInfo;

    private String expressPoint;

    public String getExpressPoint() {
        return JSONObject.parseObject(this.getExpressInfo(), ExpressOrder.ExpressInfo.class).getExpressName();
    }
}
