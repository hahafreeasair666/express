package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class AddUpdateAddressVO {

    private Integer id;

    @NotNull(message = "地址信息不能为空")
    @NotBlank(message = "地址信息不能为空")
    private String addressInfo;

    @NotNull(message = "收货人不能为空")
    @NotBlank(message = "收货人不能为空")
    private String name;

    @NotNull(message = "电话不能为空")
    @NotBlank(message = "电话不能为空")
    private String mobile;

    @NotNull(message = "经纬度不能为空")
    @NotBlank(message = "经纬度不能为空")
    private String position;

}
