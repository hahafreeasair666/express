package com.ch999.express.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.DetailedLog;
import com.ch999.express.admin.entity.Recharge;
import com.ch999.express.admin.mapper.RechargeMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.DetailedLogService;
import com.ch999.express.admin.service.RechargeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2018-04-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, Recharge> implements RechargeService {

    @Resource
    private UserWalletBORepository userWalletBORepository;

    @Resource
    private DetailedLogService detailedLogService;

    @Override
    public Map<String, Object> rechargeCode(Integer userId, String code) {
        Map<String, Object> map = new HashMap<>();
        Recharge recharge = this.selectOne(new EntityWrapper<Recharge>().eq("code", code));
        if(recharge == null){
            map.put("code",5000);
            map.put("msg","充值码错误，请检查后再充值");
            return map;
        }else if(recharge.getUsedFlag()){
            map.put("code",5000);
            map.put("msg","该充值码已被使用");
            return map;
        }else {
            UserWalletBO one = userWalletBORepository.findOne(userId);
            one.setBalance(one.getBalance() + recharge.getPrice());
            userWalletBORepository.save(one);
            detailedLogService.insert(new DetailedLog(userId,1,"充值：+"+recharge.getPrice()));
            recharge.setUsedFlag(true);
            this.updateById(recharge);
            map.put("code",0);
            map.put("msg","充值成功：充值 " + recharge.getPrice() + " 余额 " + one.getBalance());
            return map;
        }
    }

    @Override
    public void recharge(Integer userId, Double price) {
        UserWalletBO one = userWalletBORepository.findOne(userId);
        one.setBalance(one.getBalance() + price);
        userWalletBORepository.save(one);
        detailedLogService.insert(new DetailedLog(userId,1,"充值：+"+price));
    }
}
