package com.ch999.express.admin.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.DetailedLog;
import com.ch999.express.admin.entity.ExpressComment;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.DetailedLogService;
import com.ch999.express.admin.service.ExpressCommentService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * @author hahalala
 */
@Data
@Slf4j
@NoArgsConstructor
public class CommentTimeTask extends TimerTask {

    private static final Integer STAR = 5;

    private Integer userId;

    private Integer orderId;

    private ExpressCommentService expressCommentService;

    private DetailedLogService detailedLogService;

    private UserWalletBORepository userWalletBORepository;

    @Override
    public void run() {
       log.info("评价检测定时器开始工作，单号：" + orderId);
        ExpressComment expressComment = expressCommentService.selectOne(new EntityWrapper<ExpressComment>().eq("express_order_id", orderId).eq("user_id", userId));
        if(expressComment == null){
            log.info("开始自动评价");
            expressCommentService.insert(new ExpressComment(orderId,5,"系统默认5星好评",userId));
            detailedLogService.insert(new DetailedLog(userId,3,"系统默认好评: 信用分 +1"));
            UserWalletBO one = userWalletBORepository.findOne(userId);
            one.setCreditNum(one.getCreditNum() + 1);
            userWalletBORepository.save(one);
        }
    }

    public CommentTimeTask(Integer userId, Integer orderId, ExpressCommentService expressCommentService, DetailedLogService detailedLogService, UserWalletBORepository userWalletBORepository) {
        this.userId = userId;
        this.orderId = orderId;
        this.expressCommentService = expressCommentService;
        this.detailedLogService = detailedLogService;
        this.userWalletBORepository = userWalletBORepository;
    }
}
