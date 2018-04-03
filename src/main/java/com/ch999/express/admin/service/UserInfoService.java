package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.UserInfo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 检测信息是否可用
     * @param type
     * @param info
     * @return
     */
    Boolean ckeckCanUse(String type,String info);

    /**
     * 注册
     * @param userName
     * @param pwd
     * @param mobile
     * @return
     */
    Boolean insertUser(String userName,String pwd,String mobile);
}
