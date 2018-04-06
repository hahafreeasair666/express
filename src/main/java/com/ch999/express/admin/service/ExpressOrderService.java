package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.ExpressOrder;
import com.baomidou.mybatisplus.service.IService;
import com.ch999.express.admin.vo.ExpressListVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface ExpressOrderService extends IService<ExpressOrder> {

    /**
     * 新增代取订单
     * @param userId
     * @param addressId
     * @param expressInfo
     * @return
     */
    Map<String,Object> addExpressOrder(Integer userId,Integer addressId,ExpressOrder.ExpressInfo expressInfo);

    /**
     * 订单支付
     * @param userId
     * @param orderId
     * @param price
     * @return
     */
    Map<String,Object> orderPayment(Integer userId,Integer orderId,Double price);

    /**
     * 取消订单
     * @param userId
     * @param orderId
     * @return
     */
    Map<String,Object> cancelOrder(Integer userId,Integer orderId);

    /**
     * 获取可接单的订单列表
     * @param position
     * @return
     */
    List<ExpressListVO> getExpressList(String position);
}
