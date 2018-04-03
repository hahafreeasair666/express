package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ExpressVO {

    private String id;

    private String title;

    private String address;

    private Map<String,Double> location;

    public ExpressVO(String id, String title, String address, Map<String, Double> location) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.location = location;
    }
}
