package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.ExpressUser;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface ExpressUserService extends IService<ExpressUser> {

    /**
     * 接单
     * @param position
     * @param orderId
     * @param userId
     * @return
     */
    Map<String,Object> addPickUp(String position,Integer orderId,Integer userId);

    /**
     * 更新接单者的实时位置的
     * @param userId
     * @param position
     */
    void updatePosition(Integer userId,String position);

    /**
     * 检查是否为代取快递状态
     * @param userId
     * @return
     */
    Boolean checkIsPickUp(Integer userId);
}
