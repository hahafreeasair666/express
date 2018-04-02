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
}
