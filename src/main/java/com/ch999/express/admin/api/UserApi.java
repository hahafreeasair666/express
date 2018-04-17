package com.ch999.express.admin.api;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ch999.common.util.excel.ExcelImport;
import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.UserComponent;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.*;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.*;
import com.ch999.express.admin.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
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

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ExpressOrderService expressOrderService;

    @Resource
    private DetailedLogService detailedLogService;

    @Resource
    private ExpressCommentService expressCommentService;

    //认证

    @PostMapping("/Authentication/v1")
    public Result<String> Authentication(@Valid AuthenticationVO authenticationVO) {
        if (authenticationVO.getIdPic().split(",").length != 2) {
            return Result.error("error", "请传入身份证正反面照片");
        }
        UserInfo loginUser = userComponent.getLoginUser();
        if (userAuthenticationService.selectOne(new EntityWrapper<UserAuthentication>().eq("user_id", loginUser.getId())) != null) {
            return Result.error("error", "您已认证过无需重复认证");
        }
        Boolean aBoolean = userAuthenticationService.userAuthentication(loginUser, authenticationVO);
        if (aBoolean) {
            //认证成功后进行钱包初始化
            userWalletBORepository.save(new UserWalletBO(loginUser.getId()));
            log.info("用户：" + loginUser.getUserName() + "认证成功 钱包初始化");
            detailedLogService.insert(new DetailedLog(loginUser.getId(), 1, "余额初始化0.0元"));
            detailedLogService.insert(new DetailedLog(loginUser.getId(), 2, "积分初始化0积分"));
            detailedLogService.insert(new DetailedLog(loginUser.getId(), 3, "信用分初始化100分"));
            return Result.success();
        }
        return Result.error("error", "认证失败");
    }


    //资料修改，认证资料不可修改

    @PostMapping("/updateUserInfo/{type}/v1")
    public Result<String> updateUserInfo(@PathVariable("type") String type,UpdateUserInfoVO updateUserInfoVO){
        return null;
    }

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
    //个人中心（积分，余额，信用分，下的订单，接的订单）

    @GetMapping("/getCenterInfo/v1")
    public Result<CenterVO> getCenterInfo(Integer userId) {
        return Result.success(userInfoService.getCenterInfo(userId, userComponent.getLoginUser()));
    }

    //发布列表

    @GetMapping("/getUserOrderList/v1")
    public Result<PageVO<UserOrderVO>> getUserOrderList(Page<UserOrderVO> page, Integer state) {
        PageVO<UserOrderVO> pageVO = new PageVO();
        Page<UserOrderVO> userOrderList = expressOrderService.getUserOrderList(page, userComponent.getLoginUser().getId(), state);
        pageVO.setList(userOrderList.getRecords());
        pageVO.setCurrentPage(page.getCurrent());
        pageVO.setTotalPage((int) Math.ceil(page.getTotal() / (double) page.getSize()));
        return Result.success(pageVO);
    }
    //取件列表

    @GetMapping("/getUserPickUpList/v1")
    public Result<PageVO<UserPickUpVO>> getUserPickUpList(Page<UserPickUpVO> page, Integer state) {
        PageVO<UserPickUpVO> pageVO = new PageVO();
        Page<UserPickUpVO> userPickUpList = expressOrderService.getUserPickUpList(page, userComponent.getLoginUser().getId(), state);
        pageVO.setList(userPickUpList.getRecords());
        pageVO.setCurrentPage(page.getCurrent());
        pageVO.setTotalPage((int) Math.ceil(page.getTotal() / (double) page.getSize()));
        return Result.success(pageVO);
    }
    //确认收货

    @PostMapping("/confirmOrder/v1")
    public Result<String> confirmOrder(Integer orderId) {
        Map<String, Object> map = expressOrderService.confirmOrder(userComponent.getLoginUser().getId(), orderId);
        if ((int) map.get("code") != 0) {
            return Result.error("error", map.get("msg").toString());
        }
        return Result.success("success", map.get("msg").toString(), null);
    }

    //评价

    @PostMapping("/addComment/v1")
    public Result<String> addComment(Integer orderId,Integer star,String comment){
        if(orderId == null){
            return Result.error("error","请传入订单号");
        }
        if(star == null){
            return Result.error("error","请传入评分");
        }
        if(StringUtils.isBlank(comment)){
            return Result.error("error","请传入评论内容");
        }
        Map<String, Object> map = expressCommentService.addComment(orderId, userComponent.getLoginUser().getId(), star, comment);
        if((int)map.get("code") != 0){
            return Result.error("error",map.get("msg").toString());
        }
        return Result.success("success",map.get("msg").toString(),null);
    }


    //查看余额，积分，信用分明细

    @GetMapping("/getDetailLog/v1")
    public Result<List<DetailedLog>> getDetailLog(Integer type) {
        return Result.success(detailedLogService.selectList(new EntityWrapper<DetailedLog>().eq("user_id", userComponent.getLoginUser().getId())
                .eq("log_type", type).orderBy("create_time",false)));
    }

    //获取最大可用积分数量

    @GetMapping("/getCanUserIntegral/v1")
    public Result<Map<String, Object>> getCanUserIntegral(Double price) {
        if (price == null) {
            return Result.error("error", "请传入订单价格");
        }
        UserWalletBO one = userWalletBORepository.findOne(userComponent.getLoginUser().getId());
        Map<String, Object> map = new HashMap<>(2);
        double integral = price * 100;
        map.put("ownedIntegral", one.getIntegral());
        map.put("canUseIntegral", one.getIntegral() < (int) integral ? one.getIntegral() : (int) integral);
        return Result.success(map);
    }

    //查看评论的列表

    @GetMapping("/getCommentList/v1")
    public Result<List<ExpressComment>> getCommentList(Integer userId){
        return Result.success(expressCommentService.getCommentList(userId == null ? userComponent.getLoginUser().getId() : userId));
    }

}
