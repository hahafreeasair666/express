package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author hahalala
 */
@Data
@ToString
@NoArgsConstructor
public class PositionByAddressVO {

    private Integer status;

    private Result result;

    @Data
    @NoArgsConstructor
    private static class Result {

        private String title;

        private Map<String, Double> location;

        private Map<String, Object> address_components;

        /**
         * 查询字符串与查询结果的文本相似度
         */
        private Double similarity;

        /**
         * 误差距离
         */
        private Integer deviation;

        /**
         * 置信度
         */
        private Integer reliability;
    }

    public String getTitle() {
        return result.title;
    }

    public Map<String, Double> getLocation() {
        return result.location;
    }

    public String getAddress() {
        return result.address_components.get("province").toString() + result.address_components.get("city") +
                result.address_components.get("district") + result.address_components.get("street") +
                result.address_components.get("street_number");
    }
}
