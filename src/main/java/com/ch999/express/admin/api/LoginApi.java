package com.ch999.express.admin.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.UserComponent;
import com.ch999.express.admin.entity.UserAuthentication;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.service.UserAuthenticationService;
import com.ch999.express.admin.service.UserInfoService;
import com.ch999.express.admin.task.PickUpTimeTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * @author hahalala
 */
@RestController
@RequestMapping("/login/api")
public class LoginApi {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Resource
    private UserComponent userComponent;

    @PostMapping("/userRegister/v1")
    public Result<String> userRegister(String userName, String pwd1, String pwd2, String mobile) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(pwd1) || StringUtils.isBlank(pwd2) || StringUtils.isBlank(mobile)) {
            return Result.error("error", "请填完所有信息");
        }
        if (!mobile.matches(UserInfo.CK)) {
            return Result.error("error", "请填写正确的手机号");
        }
        if (!userInfoService.checkCanUse("user_name", userName)) {
            return Result.error("error", "改用户名已被使用");
        }
        if (!userInfoService.checkCanUse("mobile", mobile)) {
            return Result.error("error", "手机号已被使用");
        }
        if (!pwd1.equals(pwd2)) {
            return Result.error("error", "两次密码输入不一致");
        }
        userInfoService.insertUser(userName, pwd1, mobile);
        return Result.success();
    }

    @PostMapping("/login/v1")
    public Result<Map<String, Object>> login(String loginInfo, String pwd, Boolean isLongLogin) {
        UserInfo userInfo = null;
        UserInfo userInfo2 = userInfoService.selectOne(new EntityWrapper<UserInfo>().eq("user_name", loginInfo).eq("pwd", pwd));
        UserInfo userInfo1 = userInfoService.selectOne(new EntityWrapper<UserInfo>().eq("mobile", loginInfo).eq("pwd", pwd));
        userInfo = userInfo1 != null ? userInfo1 : userInfo2;
        if (userInfo == null) {
            return Result.error("error", "登录失败用户名密码不匹配");
        }
        Map<String, Object> map = new HashMap<>();
        String authorization = userComponent.getAuthorization(userInfo.getId(), isLongLogin == null ? false : isLongLogin);
        //查看是否认证了
        if (userAuthenticationService.selectOne(new EntityWrapper<UserAuthentication>().eq("user_id", userInfo.getId())) == null) {
            map.put("isCertified", false);
        } else {
            map.put("isCertified", true);
        }
        map.put("authorization", authorization);
        map.put("userId", userInfo.getId());
        map.put("userName", userInfo.getUserName());
        map.put("avatar", userInfo.getAvatar());
        return Result.success(map);
    }

    @GetMapping("/loginOut/v1")
    public Result<String> loginOut() {
        Boolean aBoolean = userComponent.loginOut();
        if (aBoolean) {
            return Result.success("success", "退出登录成功", null);
        }
        return Result.error("error", "退出登录失败,用户未登录");
    }

    @PostMapping("/checkUserNameOrMobileIsCanUse/v1")
    public Result<String> checkUserNameOrMobileIsCanUse(String type, String info) {
        if (type.equals("userName")) {
            if (userInfoService.checkCanUse("user_name", info)) {
                return Result.success("success", "恭喜该用户名可以使用", null);
            }
            return Result.error("error", "很遗憾该用户名已被使用");
        } else {
            if (userInfoService.checkCanUse("mobile", info)) {
                return Result.success("success", "恭喜该手机号可以使用", null);
            }
            return Result.error("error", "很遗憾该手机号已被使用");
        }
    }
}
