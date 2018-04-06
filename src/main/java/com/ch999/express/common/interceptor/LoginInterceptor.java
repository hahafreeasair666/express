package com.ch999.express.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.UserComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hahalala
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private UserComponent userComponent;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Result<String> result = new Result<>(Result.UNLOGIN, "unLogin", "请登录后再操作");
        String resultJson = JSON.toJSONString(result);
        if(userComponent.getLoginUser().getId() == null){
            log.info("拦截到未登录请求");
            httpServletResponse.setCharacterEncoding("UTF-8");
            try {
                httpServletResponse.getWriter().write(resultJson);
            } catch (IOException e) {
                log.error("登录拦截器异常",e);
            }
            httpServletResponse.setContentType("application/json");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
