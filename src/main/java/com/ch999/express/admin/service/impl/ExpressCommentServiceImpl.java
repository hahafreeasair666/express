package com.ch999.express.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.DetailedLog;
import com.ch999.express.admin.entity.ExpressComment;
import com.ch999.express.admin.entity.ExpressOrder;
import com.ch999.express.admin.entity.ExpressUser;
import com.ch999.express.admin.mapper.ExpressCommentMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.DetailedLogService;
import com.ch999.express.admin.service.ExpressCommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.service.ExpressOrderService;
import com.ch999.express.admin.service.ExpressUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@Service
@Slf4j
public class ExpressCommentServiceImpl extends ServiceImpl<ExpressCommentMapper, ExpressComment> implements ExpressCommentService {

    @Resource
    private ExpressUserService expressUserService;

    @Resource
    private ExpressOrderService expressOrderService;

    @Resource
    private DetailedLogService detailedLogService;

    @Resource
    private UserWalletBORepository userWalletBORepository;

    @Override
    public Map<String, Object> addComment(Integer orderId, Integer userId, Integer star, String comment) {
        Map<String, Object> map = new HashMap<>(2);
        ExpressOrder expressOrder = expressOrderService.selectOne(new EntityWrapper<ExpressOrder>().eq("create_user", userId).eq("id", orderId));
        if(expressOrder == null){
            map.put("code",5000);
            map.put("msg","订单查询失败");
            return map;
        }else if(expressOrder.getHandleState() != 3){
            map.put("code",5000);
            map.put("msg","该订单为不可评价状态");
            return map;
        }else {
            // 先插入评价表再进行代取者信用分的计算
            ExpressUser expressUser = expressUserService.selectOne(new EntityWrapper<ExpressUser>().eq("express_order_id", orderId).eq("complete_flag", 1));
            this.insert(new ExpressComment(orderId,star,comment,expressUser.getUserId()));
            UserWalletBO one = userWalletBORepository.findOne(expressUser.getUserId());
            Integer num = getNum(star);
            one.setIntegral(one.getIntegral() + num);
            log.info("用户评价，修改代取者信用分：" + num +" 订单号："+ userId);
            userWalletBORepository.save(one);
            detailedLogService.insert(new DetailedLog(expressUser.getUserId(),3,"用户评价  信用分变更 " + num));
            expressOrder.setHandleState(4);
            expressOrderService.updateById(expressOrder);
            map.put("code",0);
            map.put("msg","评价成功");
            return map;
        }
    }

    @Override
    public List<ExpressComment> getCommentList(Integer userId) {
        return this.selectList(new EntityWrapper<ExpressComment>().eq("user_id",userId));
    }


    private Integer getNum(Integer star){
         switch (star){
             case 1: return -5;
             case 2: return -3;
             case 3: return -1;
             case 4: return 1;
             case 5: return 2;
             default:return 0;
         }
    }
}
