package com.ch999.express.admin.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.ExpressComponent;
import com.ch999.express.admin.component.UserComponent;
import com.ch999.express.admin.entity.ExpressOrder;
import com.ch999.express.admin.entity.UserAuthentication;
import com.ch999.express.admin.service.ExpressOrderService;
import com.ch999.express.admin.service.UserAuthenticationService;
import com.ch999.express.admin.vo.ExpressListVO;
import com.ch999.express.admin.vo.ExpressVO;
import com.ch999.express.common.MapTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private UserAuthenticationService userAuthenticationService;

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
        if(userAuthenticationService.selectOne(new EntityWrapper<UserAuthentication>().eq("user_id",userId)) == null){
            return Result.error("error","您为进行身份认证，请认证后再下订单");
        }
        Map<String, Object> map = expressOrderService.addExpressOrder(userId, addressId, expressInfo);
        if(map == null){
            return Result.error("error","收货地址距快递点距离太远下单失败");
        }
        return Result.success(map);
    }

    @PostMapping("/orderPayment/v1")
    public Result<String> orderPayment(Integer orderId,Double price){
        if(orderId == null){
            return Result.error("error","请传入订单号");
        }
        if(price == null){
            return Result.error("error","请传入金额");
        }
        Map<String, Object> map = expressOrderService.orderPayment(userComponent.getLoginUser().getId(), orderId, price);
        if((int)map.get("code") != 0){
            return Result.error("error",map.get("msg").toString());
        }
        return Result.success("success",map.get("msg").toString(),null);
    }

    @PostMapping("/cancelOrder/v1")
    public Result<String> cancelOrder(Integer orderId){
        if(orderId == null){
            return Result.error("error","请传入订单号");
        }
        Map<String, Object> map = expressOrderService.cancelOrder(userComponent.getLoginUser().getId(), orderId);
        if((int)map.get("code") != 0){
            return Result.error("error",map.get("msg").toString());
        }
        return Result.success("success",map.get("msg").toString(),null);
    }


    //接代取(获取当前接件这position，返回离快递点距离，收货地址距离，所需时间等信息)

    @GetMapping("/getExpressList/v1")
    public Result<List<ExpressListVO>> getExpressList(String position){
        if(StringUtils.isBlank(position)){
            return Result.error("error","请允许获取地理位置再使用本功能");
        }
        return Result.success(expressOrderService.getExpressList(position));
    }

    @PostMapping("/addPickUp/v1")
    public Result<Object> addPickUp(String position, Integer addressId, Object obj) {
        return null;
    }
    //取消发送

    //取消接收

    //位置距离

    //完成并评价(关系取件者信用积分)
}
