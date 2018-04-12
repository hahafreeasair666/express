package com.ch999.express.admin.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.ch999.express.admin.entity.ExpressOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ch999.express.admin.vo.ExpressListVO;
import com.ch999.express.admin.vo.UserOrderVO;
import com.ch999.express.admin.vo.UserPickUpVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface ExpressOrderMapper extends BaseMapper<ExpressOrder> {

    /**
     * 大厅查看可接单列表的
     * @param page
     * @param userId
     * @return
     */
    List<ExpressOrder> getOrderList(Page<ExpressListVO> page,Integer userId);

    /**
     * 个人中心发布的订单
     * @param page
     * @param userId
     * @return
     */
    List<ExpressOrder> getUserOrderList(Page<UserOrderVO> page, Integer userId);

    /**
     * 个人中心帮人代取的订单
     * @param page
     * @param userId
     * @return
     */
    List<UserPickUpVO> selectUserPickUp(Page<UserPickUpVO> page, Integer userId);
}
