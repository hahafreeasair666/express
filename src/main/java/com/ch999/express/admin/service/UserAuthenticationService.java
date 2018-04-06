package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.UserAuthentication;
import com.baomidou.mybatisplus.service.IService;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.vo.AuthenticationVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface UserAuthenticationService extends IService<UserAuthentication> {

    /**
     * 用户认证
     * @param loginUser
     * @param authenticationVO
     * @return
     */
    Boolean userAuthentication(UserInfo loginUser, AuthenticationVO authenticationVO);
}
