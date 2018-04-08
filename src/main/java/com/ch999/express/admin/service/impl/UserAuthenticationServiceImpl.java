package com.ch999.express.admin.service.impl;

import com.ch999.express.admin.entity.UserAuthentication;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.UserAuthenticationMapper;
import com.ch999.express.admin.service.UserAuthenticationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.service.UserInfoService;
import com.ch999.express.admin.vo.AuthenticationVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2018-04-02
 */
@Service
public class UserAuthenticationServiceImpl extends ServiceImpl<UserAuthenticationMapper, UserAuthentication> implements UserAuthenticationService {

    @Resource
    private UserInfoService userInfoService;

    @Override
    public Boolean userAuthentication(UserInfo loginUser, AuthenticationVO authenticationVO) {
        boolean insert = this.insert(new UserAuthentication(loginUser.getId(), authenticationVO.getIdNumber(), authenticationVO.getIdPic()));
        loginUser.setStudentNumber(authenticationVO.getStudentNumber());
        loginUser.setRealName(authenticationVO.getRealName());
        boolean b = userInfoService.updateById(loginUser);
        return insert && b;
    }
}
