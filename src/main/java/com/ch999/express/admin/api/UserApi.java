package com.ch999.express.admin.api;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.common.util.excel.ExcelImport;
import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.UserComponent;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.UserAuthentication;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.AddressService;
import com.ch999.express.admin.service.UserAuthenticationService;
import com.ch999.express.admin.vo.AddUpdateAddressVO;
import com.ch999.express.admin.vo.AuthenticationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author hahalala
 */
@RestController
@RequestMapping("/user/api")
@Slf4j
public class UserApi {

    @Resource
    private UserComponent userComponent;

    @Resource
    private AddressService addressService;

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Resource
    private UserWalletBORepository userWalletBORepository;

    //认证

    @PostMapping("/Authentication/v1")
    public Result<String> Authentication(@Valid AuthenticationVO authenticationVO) {
        if (authenticationVO.getIdPic().split(",").length != 2) {
            return Result.error("error","请传入身份证正反面照片");
        }
        UserInfo loginUser = userComponent.getLoginUser();
        if(userAuthenticationService.selectOne(new EntityWrapper<UserAuthentication>().eq("user_id",loginUser.getId())) != null){
            return Result.error("error","您已认证过无需重复认证");
        }
        Boolean aBoolean = userAuthenticationService.userAuthentication(loginUser, authenticationVO);
        if(aBoolean){
            //认证成功后进行钱包初始化
            userWalletBORepository.save(new UserWalletBO(loginUser.getId()));
            log.info("用户：" + loginUser.getUserName() + "认证成功 钱包初始化");
            return Result.success();
        }
        return Result.error("error","认证失败");
    }


    //资料修改，认证资料不可修改


    //地址crud

    @PostMapping("/addOrUpdateAddress/v1")
    public Result<String> addOrUpdateAddress(@Valid AddUpdateAddressVO addUpdateAddressVO) {
        Boolean aBoolean = addressService.addOrUpdateAddress(userComponent.getLoginUser().getId(), addUpdateAddressVO);
        if (!aBoolean) {
            return Result.error("error", "操作失败");
        }
        return Result.success();
    }

    @GetMapping("/getAddressList/v1")
    public Result<List<Map<String, Object>>> getAddressList() {
        return Result.success(addressService.getAddressListByUserId(userComponent.getLoginUser().getId()));
    }

    @GetMapping("/getAddressById/v1")
    public Result<Map<String, Object>> getAddressById(Integer id) {
        if (id == null) {
            return Result.error("error", "请传入地址id");
        }
        return Result.success(addressService.getAddressById(id));
    }

    @PostMapping("/deleteAddressById/v1")
    public Result<String> deleteAddressById(Integer id) {
        if (id == null) {
            return Result.error("error", "请传入地址id");
        }
        Boolean aBoolean = addressService.deleteAddressById(userComponent.getLoginUser().getId(), id);
        if (!aBoolean) {
            return Result.error("error", "操作失败");
        }
        return Result.success();
    }
    //个人中心

    //发布列表

    //取件列表

    //从发布，取件点进去看评价，进行中的单要查看距离


}
