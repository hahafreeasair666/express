package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class CenterVO {

    private Integer userId;

    private Integer creditNum;

    private String userName;

    private String avatar;

    private Integer commentCount;

    private Object score;

}
