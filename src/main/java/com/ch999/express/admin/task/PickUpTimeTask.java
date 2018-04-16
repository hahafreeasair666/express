package com.ch999.express.admin.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.DetailedLog;
import com.ch999.express.admin.entity.ExpressUser;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.DetailedLogService;
import com.ch999.express.admin.service.ExpressUserService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

/**
 * @author hahalala
 */
@Data
@Slf4j
@NoArgsConstructor
public class PickUpTimeTask extends TimerTask {

    private Integer orderId;

    private Integer userId;

    private ExpressUserService expressUserService;

    private UserWalletBORepository userWalletBORepository;

    private DetailedLogService detailedLogService;

    @Override
    public void run() {
        log.info("按时送达检测定时器任务开始：" + orderId);
        ExpressUser expressUser = expressUserService.selectOne(new EntityWrapper<ExpressUser>().eq("userId", userId).eq("express_order_id", orderId));
        if (!expressUser.getCompleteFlag()) {
            log.info("用户：" + userId + " 订单：" + orderId + " 两小时未送达");
            UserWalletBO one = userWalletBORepository.findOne(userId);
            one.setCreditNum(one.getCreditNum() - 2);
            userWalletBORepository.save(one);
            detailedLogService.insert(new DetailedLog(userId,3,"代取未按时送达: 信用分 -2"));
        }
    }

    public PickUpTimeTask(Integer orderId, Integer userId, ExpressUserService expressUserService, UserWalletBORepository userWalletBORepository, DetailedLogService detailedLogService) {
        this.orderId = orderId;
        this.userId = userId;
        this.expressUserService = expressUserService;
        this.userWalletBORepository = userWalletBORepository;
        this.detailedLogService = detailedLogService;
    }
}
