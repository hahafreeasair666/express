package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.Recharge;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2018-04-17
 */
public interface RechargeService extends IService<Recharge> {

    /**
     * 充值码充值
     * @param userId
     * @param code
     * @return
     */
    Map<String,Object> rechargeCode(Integer userId,String code);

    /**
     * 按金额充值
     * @param userId
     * @param price
     */
    void recharge(Integer userId,Double price);

}
