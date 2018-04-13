package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.ExpressComment;
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
public interface ExpressCommentService extends IService<ExpressComment> {

    /**
     * 订单评价
     * @param orderId
     * @param userId
     * @param star
     * @param comment
     * @return
     */
    Map<String,Object> addComment(Integer orderId,Integer userId,Integer star,String comment);
}
