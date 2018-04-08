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
public class AuthenticationVO {

    /**
     * 真实姓名
     */
    @NotNull(message = "真实姓名不能为空")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 身份证号
     */
    @NotNull(message = "身份证号码不能为空")
    @NotBlank(message = "身份证号码不能为空")
    private String idNumber;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 身份证正反面照片
     */
    @NotNull(message = "身份证号码不能为空")
    @NotBlank(message = "身份证号码不能为空")
    private String idPic;
}
