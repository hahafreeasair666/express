package com.ch999.express.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.ExpressOrder;
import com.ch999.express.admin.entity.ExpressUser;
import com.ch999.express.admin.mapper.ExpressUserMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.DetailedLogService;
import com.ch999.express.admin.service.ExpressOrderService;
import com.ch999.express.admin.service.ExpressUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.task.PickUpTimeTask;
import com.ch999.express.common.MapTools;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;

import static com.ch999.express.common.Distance.getDistance;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@Service
public class ExpressUserServiceImpl extends ServiceImpl<ExpressUserMapper, ExpressUser> implements ExpressUserService {

    @Resource
    private ExpressOrderService expressOrderService;

    @Resource
    private UserWalletBORepository userWalletBORepository;

    @Resource
    private ExpressUserService expressUserService;

    @Resource
    private DetailedLogService detailedLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> addPickUp(String position,Integer orderId,Integer userId) {
        Map<String,Object> map = new HashMap<>();
        ExpressOrder expressOrder = expressOrderService.selectById(orderId);
        if(expressOrder == null){
            map.put("code",5000);
            map.put("msg","订单不存在，或已被取消，再看看别的订单吧");
            return map;
        }else if(expressOrder.getHandleState() != 1){
            map.put("code",5000);
            map.put("msg","订单已被别人抢先一步接了，再看看别的订单吧");
            return map;
        }else {
            if(expressOrder.getCreateUser().equals(userId)){
                map.put("code",5000);
                map.put("msg","不能自己接自己的订单，不想代取了就去取消订单吧");
                return map;
            }
            ExpressOrder.ExpressInfo expressInfo = JSONObject.parseObject(expressOrder.getExpressInfo(), ExpressOrder.ExpressInfo.class);
            if(getDistance(position,expressInfo.getPosition()) > 10000.0){
                map.put("code",5000);
                map.put("msg","您距快递点距离过远，还是把机会让给别人吧");
                return map;
            }
            UserWalletBO one = userWalletBORepository.findOne(userId);
            if(one.getCreditNum() < 90){
                map.put("code",5000);
                map.put("msg","您的信用分过低，暂时不能接单，请联系开发人员整改后再接单");
                return map;
            }
            List<ExpressUser> expressUsers = this.selectList(new EntityWrapper<ExpressUser>().eq("userId", userId).eq("complete_flag", 0));
            if(expressUsers.size() > 3){
                map.put("code",5000);
                map.put("msg","抱歉一次最多只能接3单，完成了再来接吧");
                return map;
            }
            ExpressUser expressUser = new ExpressUser(orderId,userId);
            expressUser.setPosition(position);
            this.insert(expressUser);
            expressOrder.setHandleState(2);
            expressOrderService.updateById(expressOrder);
            map.put("code",0);
            map.put("msg","恭喜成功接单，快去帮去快递吧");
            Timer timer = new Timer("接单后检测完成状态的定时器");
            //注意延迟是毫秒数
            timer.schedule(new PickUpTimeTask(orderId,userId,expressUserService,userWalletBORepository,detailedLogService),7200000L);
            return map;
        }
    }

    @Override
    public void updatePosition(Integer userId, String position) {
        this.selectList(new EntityWrapper<ExpressUser>().eq("userId",userId).eq("complete_flag",0)).forEach(li->{
            li.setPosition(position);
            this.updateById(li);
        });
    }

    @Override
    public Boolean checkIsPickUp(Integer userId) {
        return CollectionUtils.isNotEmpty(this.selectList(new EntityWrapper<ExpressUser>().eq("userId",userId).eq("complete_flag",0)));
    }
}
