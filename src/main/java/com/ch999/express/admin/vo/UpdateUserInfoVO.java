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

    private String userName;

    private String oldPwd;

    private String newPwd;

    private String mobile;

    private MultipartFile file;
}
