package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hahalala
 */
@Data
@NoArgsConstructor
public class UpdateUserInfoVO {

    public static final String NAME = "name";
    public static final String PWD = "pwd";
    public static final String AVATAR = "avatar";
    public static final String MOBILE = "mobile";
    public static final String CK = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[6])|(18[0,5-9])|(19[9]))\\d{8}$";

    private String userName;

    private String oldPwd;

    private String newPwd;

    private String mobile;

    private MultipartFile file;
}
