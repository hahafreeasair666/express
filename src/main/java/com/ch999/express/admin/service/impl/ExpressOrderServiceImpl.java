package com.ch999.express.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.Address;
import com.ch999.express.admin.entity.ExpressOrder;
import com.ch999.express.admin.entity.ExpressUser;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.ExpressOrderMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.AddressService;
import com.ch999.express.admin.service.ExpressOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.service.ExpressUserService;
import com.ch999.express.admin.service.UserInfoService;
import com.ch999.express.admin.vo.ExpressDetailVO;
import com.ch999.express.admin.vo.ExpressListVO;
import com.ch999.express.common.MapTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2018-04-02
 */
@Service
@Slf4j
public class ExpressOrderServiceImpl extends ServiceImpl<ExpressOrderMapper, ExpressOrder> implements ExpressOrderService {

    @Resource
    private AddressService addressService;

    @Resource
    private UserWalletBORepository userWalletBORepository;

    @Resource
    private ExpressUserService expressUserService;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public Map<String, Object> addExpressOrder(Integer userId, Integer addressId, ExpressOrder.ExpressInfo expressInfo) {
        //先验算距离，收货地址到快递点超过10km不让发布
        Address.AddressInfoVO addressInfoVO = JSONObject.parseObject(addressService.getAddressById(addressId).get("addressInfo").toString(), Address.AddressInfoVO.class);
        Integer distanceByPosition = MapTools.getDistanceByPosition(addressInfoVO.getPosition(), expressInfo.getPosition());
        if (distanceByPosition == -1) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        ExpressOrder expressOrder = new ExpressOrder(userId, addressId);
        Double price = handlePrice(expressInfo.getWeight(), distanceByPosition, expressInfo.getTip());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expressName", expressInfo.getExpressName());
        jsonObject.put("expressAddress", expressInfo.getExpressAddress());
        jsonObject.put("expressMobile", expressInfo.getExpressMobile());
        jsonObject.put("position", expressInfo.getPosition());
        jsonObject.put("code", expressInfo.getCode());
        jsonObject.put("weight", expressInfo.getWeight());
        jsonObject.put("price", price);
        expressOrder.setExpressInfo(jsonObject.toJSONString());
        this.insert(expressOrder);
        map.put("orderId", expressOrder.getId());
        map.put("price", price);
        map.put("tips", "下单成功快去支付吧");
        return map;
    }

    @Override
    public Map<String, Object> orderPayment(Integer userId, Integer orderId, Double price) {
        Map<String, Object> map = new HashMap<>();
        //校验订单是否已经支付
        ExpressOrder expressOrder = this.selectById(orderId);
        if (expressOrder == null) {
            map.put("code", 5000);
            map.put("msg", "订单不存在，请检查订单号");
            return map;
        } else if (expressOrder.getHandleState() != 0) {
            map.put("code", 5000);
            map.put("msg", "订单已支付，无需重复支付");
            return map;
        }
        //校验金额是否正确
        else if (JSONObject.parseObject(expressOrder.getExpressInfo(), ExpressOrder.ExpressInfo.class).getPrice().doubleValue() != price.doubleValue()) {
            map.put("code", 5000);
            map.put("msg", "订单金额不正确，请检查后再支付");
            return map;
        }
        //校验余额是否充足
        UserWalletBO one = userWalletBORepository.findOne(userId);
        if (one == null) {
            map.put("code", 5000);
            map.put("msg", "您还未进行认证，无法完成支付");
            return map;
        } else if (one.getBalance() < price) {
            map.put("code", 5000);
            map.put("msg", "余额不足，请充值后再进行支付");
            return map;
        } else {
            map.put("code", 0);
            map.put("msg", "支付成功，本次支付：" + price + "元，余额：" + (one.getBalance() - price) + "元,获得：" + price.intValue() + "积分");
            one.setBalance(one.getBalance() - price);
            one.setIntegral(one.getIntegral() + price.intValue());
            userWalletBORepository.save(one);
            expressOrder.setHandleState(1);
            this.updateById(expressOrder);
            log.info(userId + "：下单成功，余额积分已处理");
            return map;
        }
    }

    @Override
    public Map<String, Object> cancelOrder(Integer userId, Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        ExpressOrder expressOrder = this.selectById(orderId);
        if (expressOrder == null) {
            map.put("code", 5000);
            map.put("msg", "订单不存在取消失败");
            return map;
        } else if (expressOrder.getHandleState() > 1) {
            map.put("code", 5000);
            map.put("msg", "该状态订单不可取消");
            return map;
        } else if (expressOrder.getHandleState() == 1) {
            UserWalletBO one = userWalletBORepository.findOne(userId);
            ExpressOrder.ExpressInfo expressInfo = JSONObject.parseObject(expressOrder.getExpressInfo(), ExpressOrder.ExpressInfo.class);
            one.setBalance(one.getBalance() + expressInfo.getPrice());
            one.setIntegral(one.getIntegral() - expressInfo.getPrice().intValue());
            userWalletBORepository.save(one);
            log.info(orderId + "订单取消成功，已支付金额返还");
        }
        map.put("code", 0);
        if (expressOrder.getHandleState() == 1) {
            map.put("msg", "订单取消成功,支付的钱已经退回到您的账户");
        } else {
            map.put("msg", "订单取消成功");
        }
        this.deleteById(orderId);
        return map;
    }

    @Override
    public List<ExpressListVO> getExpressList(String position, Boolean sortByPrice, Boolean sortByDistance1, Boolean sortByDistance2) {
        List<ExpressListVO> listVOS = new ArrayList<>();
        this.selectList(new EntityWrapper<ExpressOrder>().eq("handle_state", 1)).forEach(li -> {
            ExpressListVO expressListVO = assembleExpressInfo(position, li, false);
            if (expressListVO != null) {
                listVOS.add(expressListVO);
            }
        });
        //排序
        List<ExpressListVO> listVOS2 = new ArrayList<>();
        if (sortByDistance2) {
            listVOS2 = listVOS.stream().sorted(Comparator.comparing(li -> (int) li.getDistanceInfo().get("distance2"))).collect(Collectors.toList());
        } else if (sortByDistance1) {
            listVOS2 = listVOS.stream().sorted(Comparator.comparing(li -> (int) li.getDistanceInfo().get("distance1"))).collect(Collectors.toList());
        } else if (sortByPrice) {
            listVOS2 = listVOS.stream().sorted(Comparator.comparing(ExpressListVO::getPrice, Comparator.reverseOrder())).collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(listVOS2) ? listVOS2 : listVOS;
    }

    @Override
    public ExpressListVO getOrderDetailById(String position, Integer userId, Integer orderId) {
        ExpressUser expressUser = expressUserService.selectOne(new EntityWrapper<ExpressUser>().eq("express_order_id", orderId).eq("userId", userId));
        if (expressUser == null) {
            return null;
        }
        ExpressOrder expressOrder = this.selectById(orderId);
        if (expressOrder != null) {
            ExpressListVO expressListVO = assembleExpressInfo(position, expressOrder, true);
            return expressListVO;
        }
        return new ExpressListVO();
    }

    @Override
    public ExpressDetailVO getOrderDetailById(Integer userId, Integer orderId) {
        ExpressDetailVO expressDetailVO = new ExpressDetailVO();
        ExpressOrder expressOrder = this.selectById(orderId);
        if (expressOrder == null) {
            return expressDetailVO;
        } else if (!expressOrder.getCreateUser().equals(userId)) {
            return null;
        } else {
            Map<String, Object> addressInfo = new HashMap<>();
            Map<String, Object> expressInfo = new HashMap<>();
            Map<String, Object> employeeInfo = new HashMap<>();
            expressDetailVO.setOrderId(orderId);
            expressDetailVO.setAddressInfo(addressInfo);
            expressDetailVO.setEmployeeInfo(employeeInfo);
            expressDetailVO.setExpressInfo(expressInfo);
            Map<String, Object> addressById = addressService.getAddressById(expressOrder.getAddress());
            Address.AddressInfoVO addressInfoVO = JSONObject.parseObject(addressById.get("addressInfo").toString(), Address.AddressInfoVO.class);
            addressInfo.put("name",addressInfoVO.getName());
            addressInfo.put("mobile",addressInfoVO.getMobile());
            addressInfo.put("address",addressInfoVO.getAddress());
            ExpressUser expressUser = expressUserService.selectOne(new EntityWrapper<ExpressUser>().eq("express_order_id", orderId));
            if(expressUser != null){
                UserInfo userInfo = userInfoService.selectById(expressUser.getUserId());
                employeeInfo.put("name",userInfo.getRealName());
                employeeInfo.put("mobile",userInfo.getMobile());
                Integer distanceByPosition = MapTools.getDistanceByPosition(expressUser.getPosition(), addressInfoVO.getPosition());
                employeeInfo.put("distance","代取快递的人距离您" + distanceByPosition +"米");
            }
            ExpressOrder.ExpressInfo expressInfo1 = JSONObject.parseObject(expressOrder.getExpressInfo(), ExpressOrder.ExpressInfo.class);
            expressInfo.put("expressName",expressInfo1.getExpressName());
            expressInfo.put("expressAddress",expressInfo1.getExpressAddress());
            expressInfo.put("expressMobile",expressInfo1.getExpressMobile());
            expressInfo.put("price",expressInfo1.getPrice());
            expressInfo.put("weight",getWeight(expressInfo1.getWeight()));
            expressInfo.put("code",expressInfo1.getCode());
            return expressDetailVO;
        }
    }

    private Double handlePrice(Integer weight, Integer distance, Double tip) {
        Integer price = 0;
        if (distance > 0 && distance < 1000) {
            price = 2;
        } else if (distance >= 1000 && distance < 2000) {
            price = 3;
        } else if (distance >= 2000) {
            Integer d = distance - 2000;
            price = 4 + d % 500;
        }
        return weight * price + tip;
    }

    private String getWeight(Integer weight) {
        switch (weight) {
            case 1:
                return "5公斤以内";
            case 2:
                return "5-10公斤";
            case 3:
                return "10公斤以上";
            default:
                return "未知";
        }
    }

    private ExpressListVO assembleExpressInfo(String position, ExpressOrder li, Boolean isGetDetail) {
        ExpressOrder.ExpressInfo expressInfo = JSONObject.parseObject(li.getExpressInfo(), ExpressOrder.ExpressInfo.class);
        Integer distance = MapTools.getDistanceByPosition(expressInfo.getPosition(), position);
        if (distance < 5000 && distance > 0) {
            ExpressListVO expressListVO = new ExpressListVO();
            expressListVO.setOrderId(li.getId());
            Map addressInfo = JSONObject.parseObject(addressService.getAddressById(li.getAddress()).get("addressInfo").toString(), Map.class);
            Map<String, Object> employerInfo = new HashMap<>();
            Map<String, Object> liExpressInfo = new HashMap<>();
            Map<String, Object> distanceInfo = new HashMap<>();
            expressListVO.setEmployerInfo(employerInfo);
            expressListVO.setExpressInfo(liExpressInfo);
            expressListVO.setDistanceInfo(distanceInfo);
            employerInfo.put("employerName", addressInfo.get("name"));
            if (isGetDetail) {
                employerInfo.put("employMobile", addressInfo.get("mobile"));
                liExpressInfo.put("expressMobile", expressInfo.getExpressMobile());
                liExpressInfo.put("code", expressInfo.getCode());
            }
            employerInfo.put("employAddress", addressInfo.get("address"));
            liExpressInfo.put("expressName", expressInfo.getExpressName());
            liExpressInfo.put("expressAddress", expressInfo.getExpressAddress());
            liExpressInfo.put("weight", getWeight(expressInfo.getWeight()));
            expressListVO.setPrice(expressInfo.getPrice());
            distanceInfo.put("distance1", distance + "米");
            distanceInfo.put("tips1", "您到达快递点步行需要" + Math.ceil(distance / MapTools.AVGWALKSPEED) + "分钟");
            Integer distance2 = MapTools.getDistanceByPosition(expressInfo.getPosition(), addressInfo.get("position").toString());
            distanceInfo.put("distance2", distance2 + "米");
            distanceInfo.put("tips2", "从快递点到收货点步行需要" + Math.ceil(distance2 / MapTools.AVGWALKSPEED) + "分钟");
            return expressListVO;
        }
        return null;
    }
}
