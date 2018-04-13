package com.ch999.express.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.ch999.express.admin.entity.ExpressOrder;
import com.baomidou.mybatisplus.service.IService;
import com.ch999.express.admin.vo.*;

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
    Map<String,Object> orderPayment(Integer userId,Integer orderId,Double price,Integer integral);

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
    Page<ExpressListVO> getExpressList(Page<ExpressListVO> page,String position,Boolean sortByPrice,Boolean sortByDistance1,Boolean sortByDistance2,Integer userId);

    /**
     * 根据订单id获取订单详情,接单者版
     * @param position
     * @param userId
     * @param orderId
     * @return
     */
    ExpressListVO getOrderDetailById(String position,Integer userId,Integer orderId);

    /**
     * 根据订单id获取订单详情,下单者版
     * @param userId
     * @param orderId
     * @return
     */
    ExpressDetailVO getOrderDetailById(Integer userId,Integer orderId);

    /**
     * 个人中心的订单列表
     * @param page
     * @param userId
     * @param state
     * @return
     */
    Page<UserOrderVO> getUserOrderList(Page<UserOrderVO> page, Integer userId,Integer state);

    /**
     * 个人中心代取列表
     * @param page
     * @param userId
     * @param state
     * @return
     */
    Page<UserPickUpVO> getUserPickUpList(Page<UserPickUpVO> page, Integer userId,Integer state);

    /**
     * 确认收货订单
     * @param userId
     * @param orderId
     * @return
     */
    Map<String,Object> confirmOrder(Integer userId,Integer orderId);
}
