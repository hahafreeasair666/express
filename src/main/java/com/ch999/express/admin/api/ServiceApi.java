package com.ch999.express.admin.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.ExpressComponent;
import com.ch999.express.admin.component.UserComponent;
import com.ch999.express.admin.entity.ExpressOrder;
import com.ch999.express.admin.entity.Image;
import com.ch999.express.admin.entity.UserAuthentication;
import com.ch999.express.admin.service.ExpressOrderService;
import com.ch999.express.admin.service.ExpressUserService;
import com.ch999.express.admin.service.ImgService;
import com.ch999.express.admin.service.UserAuthenticationService;
import com.ch999.express.admin.vo.ExpressDetailVO;
import com.ch999.express.admin.vo.ExpressListVO;
import com.ch999.express.admin.vo.ExpressVO;
import com.ch999.express.admin.vo.PageVO;
import com.ch999.express.common.MapTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/service/api")
public class ServiceApi {

    @Resource
    private UserComponent userComponent;

    @Resource
    private ExpressComponent expressComponent;

    @Resource
    private ExpressOrderService expressOrderService;

    @Resource
    private ExpressUserService expressUserService;

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Resource
    private ImgService imgService;

    //图片上传

    @PostMapping("/uploadFile/v1")
    public Result<Image> uploadFile(MultipartFile file){
        if(file == null){
            return Result.error("error","请传入图片");
        }
        return Result.success(imgService.uploadImg(file));
    }

    //发代取请求

    @GetMapping("/getExpressPoint/v1")
    public Result<List> getExpressPoint(Integer addressId) {
        if (addressId == null) {
            return Result.error("error", "请传入地址id");
        }
        return Result.success(expressComponent.getExpressList(addressId));
    }

    @GetMapping("/getAddressByKeyWord/v1")
    public Result<List<ExpressVO>> getAddressByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord)) {
            return Result.error("error", "请传入关键字");
        }
        return Result.success(MapTools.getAddressByKeyWord(keyWord));
    }

    @PostMapping("/addExpressOrder/v1")
    public Result<Map<String, Object>> addExpressOrder(Integer addressId, @Valid ExpressOrder.ExpressInfo expressInfo) {
        //没认证不能下单
        Integer userId = userComponent.getLoginUser().getId();
        if (userAuthenticationService.selectOne(new EntityWrapper<UserAuthentication>().eq("user_id", userId)) == null) {
            return Result.error("error", "您未进行身份认证，请认证后再下订单");
        }
        Map<String, Object> map = expressOrderService.addExpressOrder(userId, addressId, expressInfo);
        if (map == null) {
            return Result.error("error", "收货地址距快递点距离太远下单失败");
        }
        return Result.success(map);
    }

    @PostMapping("/orderPayment/v1")
    public Result<String> orderPayment(Integer orderId, Double price,Integer integral) {
        if (orderId == null) {
            return Result.error("error", "请传入订单号");
        }
        if (price == null) {
            return Result.error("error", "请传入金额");
        }
        Map<String, Object> map = expressOrderService.orderPayment(userComponent.getLoginUser().getId(), orderId, price,integral);
        if ((int) map.get("code") != 0) {
            return Result.error("error", map.get("msg").toString());
        }
        return Result.success("success", map.get("msg").toString(), null);
    }

    @PostMapping("/cancelOrder/v1")
    public Result<String> cancelOrder(Integer orderId) {
        if (orderId == null) {
            return Result.error("error", "请传入订单号");
        }
        Map<String, Object> map = expressOrderService.cancelOrder(userComponent.getLoginUser().getId(), orderId);
        if ((int) map.get("code") != 0) {
            return Result.error("error", map.get("msg").toString());
        }
        return Result.success("success", map.get("msg").toString(), null);
    }


    //接代取(获取当前接件这position，返回离快递点距离，收货地址距离，所需时间等信息)

    @GetMapping("/getExpressList/v1")
    public Result<PageVO<ExpressListVO>> getExpressList(Page<ExpressListVO> page,String position, Boolean sortByPrice, Boolean sortByDistance1, Boolean sortByDistance2, Integer userId) {
        if (StringUtils.isBlank(position)) {
            return Result.error("error", "请允许获取地理位置再使用本功能");
        }
        Page<ExpressListVO> expressList = expressOrderService.getExpressList(page, position, sortByPrice == null ? false : sortByPrice, sortByDistance1 == null ? false : sortByDistance1, sortByDistance2 == null ? false : sortByDistance2, userId);
        PageVO<ExpressListVO> pageVO = new PageVO<>();
        pageVO.setList(expressList.getRecords());
        pageVO.setCurrentPage(page.getCurrent());
        pageVO.setTotalPage((int) Math.ceil(page.getTotal() / (double) page.getSize()));
        return Result.success(pageVO);
    }

    @PostMapping("/addPickUp/v1")
    public Result<Map<String,Object>> addPickUp(String position,Integer orderId) {
        //没认证不能下单
        Integer userId = userComponent.getLoginUser().getId();
        if (userAuthenticationService.selectOne(new EntityWrapper<UserAuthentication>().eq("user_id", userId)) == null) {
            return Result.error("error", "您未进行身份认证，请认证后再接单");
        }
        if(orderId == null){
            return Result.error("error","请传入订单id");
        }
        if(StringUtils.isBlank(position)){
            return Result.error("error","请允许获取设备位置后再进行接单");
        }
        Map<String, Object> map = expressUserService.addPickUp(position,orderId, userId);
        if((int)map.get("code") != 0){
            return Result.error("error",map.get("msg").toString());
        }
        Map<String,Object> map1 = new HashMap<>();
        map1.put("orderId",orderId);
        return Result.success("success",map.get("msg").toString(),map1);
    }

    @GetMapping("/getOrderDetailById/v1")
    public Result<ExpressListVO> getOrderDetailById(String position,Integer orderId){
        if(orderId == null){
            return Result.error("error","请传入订单号");
        }
        if(StringUtils.isBlank(position)){
            return Result.error("error","请允许获取手机位置后再来查看");
        }
        ExpressListVO orderDetailById = expressOrderService.getOrderDetailById(position, 4, orderId);
        if(orderDetailById == null){
            return Result.error("error","改订单不是您的，无权查看详细信息");
        }else if(orderDetailById.getOrderId() == null){
            return Result.error("error","订单查询失败");
        }else {
            return Result.success(orderDetailById);
        }
    }

    @PostMapping("/updatePosition/v1")
    public Result<String> updatePosition(String position){
        if(StringUtils.isBlank(position)){
            return Result.error("error","请传入要更新的坐标");
        }
        expressUserService.updatePosition(userComponent.getLoginUser().getId(),position);
        return Result.success();
    }

    @GetMapping("/checkIsPickUp/v1")
    public Result<Boolean> checkIsPickUp(){
       return Result.success(expressUserService.checkIsPickUp(userComponent.getLoginUser().getId()));
    }

    @GetMapping("/getOrderDetailById2/v1")
    public Result<ExpressDetailVO> getOrderDetailById(Integer orderId){
        if(orderId == null){
            return Result.error("error","请传入订单号");
        }
        ExpressDetailVO orderDetailById = expressOrderService.getOrderDetailById(userComponent.getLoginUser().getId(), orderId);
        if(orderDetailById == null){
            return Result.error("error","只能查看自己下的订单");
        }else if(orderDetailById.getOrderId() == null){
            return Result.error("error","查看订单详情失败");
        }
        return Result.success(orderDetailById);
    }

    //取消发送

    //取消接收

    //位置距离

    //完成并评价(关系取件者信用积分)
}
