package com.ch999.express.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.UserInfoMapper;
import com.ch999.express.admin.service.UserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public Boolean checkCanUse(String type, String info) {
        return this.selectOne(new EntityWrapper<UserInfo>().eq(type,info)) == null;
    }

    @Override
    public Boolean insertUser(String userName, String pwd, String mobile) {
        return this.insert(new UserInfo(userName,pwd,mobile));
    }
}
