package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.UserInfo;
import com.baomidou.mybatisplus.service.IService;
import com.ch999.express.admin.vo.CenterVO;

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
    Boolean checkCanUse(String type,String info);

    /**
     * 注册
     * @param userName
     * @param pwd
     * @param mobile
     * @return
     */
    Boolean insertUser(String userName,String pwd,String mobile);

    /**
     * 获取个人中心信息
     * @param userId
     * @return
     */
    CenterVO getCenterInfo(Integer userId,UserInfo userInfo);
}
