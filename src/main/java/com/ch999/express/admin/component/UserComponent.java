package com.ch999.express.admin.component;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.ch999.express.admin.document.ExpressUserBO;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.repository.ExpressUserBORepository;
import com.ch999.express.admin.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hahalala
 */
@Component
@Slf4j
public class UserComponent {

    private static final String USERID_PIX = "GetExpressUserInfo:";

    private static final String AUTHORIZATION = "Authorization";

    @Resource
    private ExpressUserBORepository expressUserBORepository;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取当前登录用户的基本信息
     * @return
     */
    public UserInfo getLoginUser(){
        Integer userId = getUserId();
        if(userId != -1){
            return userInfoService.selectById(userId);
        }
        return new UserInfo();
    }

    public String getAuthorization(Integer userId, Boolean isLogonFree) {
        final String authorization = IdWorker.get32UUID();
        ExpressUserBO userBO = new ExpressUserBO(USERID_PIX + authorization, userId);
        if (isLogonFree) {
            userBO.setLiveTime(604800);
        }
        expressUserBORepository.save(userBO);
        return authorization;
    }

    private Integer getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(AUTHORIZATION);
        log.info("token :" + token);
        ExpressUserBO user = expressUserBORepository.findOne(USERID_PIX + token);
        return user == null ? -1 : user.getUserId();
    }

    public Boolean loginOut() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        expressUserBORepository.delete(USERID_PIX + token);
        ExpressUserBO user = expressUserBORepository.findOne(USERID_PIX + token);
        return user == null;
    }
}
