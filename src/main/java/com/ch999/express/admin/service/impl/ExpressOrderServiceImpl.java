package com.ch999.express.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.*;
import com.ch999.express.admin.mapper.ExpressOrderMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.vo.*;
import com.ch999.express.common.MapTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private ExpressOrderMapper expressOrderMapper;

    @Resource
    private DetailedLogService detailedLogService;

    @Resource
    private ExpressCommentService expressCommentService;

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
        jsonObject.put("expressMsg", expressInfo.getExpressMsg());
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
    public Map<String, Object> orderPayment(Integer userId, Integer orderId, Double price,Integer integral) {
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
        } else if (one.getBalance() + integral/100.0 < price) {
            map.put("code", 5000);
            map.put("msg", "余额不足，请充值后再进行支付");
            return map;
        } else {
            map.put("code", 0);
            map.put("msg", "支付成功，本次支付：" + price + "元，余额：" + (one.getBalance() - price + integral/100.0) + "元,获得：" + (price.intValue() - (int)Math.ceil(integral/100.0)) + "积分");
            one.setBalance(one.getBalance() - price);
            one.setIntegral(one.getIntegral() + price.intValue() - integral);
            userWalletBORepository.save(one);
            expressOrder.setHandleState(1);
            detailedLogService.insert(new DetailedLog(userId,1,"下单：余额 -"+price+"元"));
            if(integral > 0){
                detailedLogService.insert(new DetailedLog(userId,2,"使用积分付款：积分 -"+integral+"分"));
            }
            if(price > integral/100.0){
                detailedLogService.insert(new DetailedLog(userId,2,"下单：积分 +"+(price.intValue() - (int)Math.ceil(integral/100.0))+"分"));
            }
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
            detailedLogService.insert(new DetailedLog(userId,1,"取消订单：余额 +"+expressInfo.getPrice()+"元"));
            detailedLogService.insert(new DetailedLog(userId,2,"取消订单：积分 -"+expressInfo.getPrice().intValue()+"分"));
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
    public Page<ExpressListVO> getExpressList(Page<ExpressListVO> page,String position, Boolean sortByPrice, Boolean sortByDistance1, Boolean sortByDistance2,Integer userId) {
        List<ExpressListVO> listVOS = new ArrayList<>();
        expressOrderMapper.getOrderList(page,userId).forEach(li->{
            ExpressListVO expressListVO = assembleExpressInfo(position, li, false,null);
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
        return CollectionUtils.isNotEmpty(listVOS2) ? page.setRecords(listVOS2) : page.setRecords(listVOS);
    }

    @Override
    public ExpressListVO getOrderDetailById(String position, Integer userId, Integer orderId) {
        ExpressUser expressUser = expressUserService.selectOne(new EntityWrapper<ExpressUser>().eq("express_order_id", orderId).eq("userId", userId));
        if (expressUser == null) {
            return null;
        }
        ExpressOrder expressOrder = this.selectById(orderId);
        if (expressOrder != null) {
            ExpressListVO expressListVO = assembleExpressInfo(position, expressOrder, true,expressUser);
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
            expressInfo.put("expressMsg",expressInfo1.getExpressMsg());
            expressInfo.put("price",expressInfo1.getPrice());
            expressInfo.put("weight",getWeight(expressInfo1.getWeight()));
            expressInfo.put("code",expressInfo1.getCode());
            expressDetailVO.setState(expressOrder.getHandleState());
            expressDetailVO.setCommentInfo(expressCommentService.selectOne(new EntityWrapper<ExpressComment>().eq("express_order_id",orderId)));
            return expressDetailVO;
        }
    }

    @Override
    public Page<UserOrderVO> getUserOrderList(Page<UserOrderVO> page, Integer userId,Integer state) {
        List<UserOrderVO> list = new ArrayList<>();
        expressOrderMapper.getUserOrderList(page,userId,state).forEach(li->{
            UserOrderVO map = new UserOrderVO();
            map.setOrderId(li.getId());
            map.setState(li.getHandleState());
            ExpressOrder.ExpressInfo expressInfo = JSONObject.parseObject(li.getExpressInfo(), ExpressOrder.ExpressInfo.class);
            map.setExpressPoint(expressInfo.getExpressName());
            list.add(map);
        });
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<UserPickUpVO> getUserPickUpList(Page<UserPickUpVO> page, Integer userId,Integer state) {
        List<UserPickUpVO> userPickUpVOS = expressOrderMapper.selectUserPickUp(page, userId,state);
        return page.setRecords(userPickUpVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmOrder(Integer userId, Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        ExpressOrder expressOrder = this.selectById(orderId);
        if(!userId.equals(expressOrder.getCreateUser())){
            map.put("code",5000);
            map.put("msg","只能确认自己的订单");
            return map;
        }else if(expressOrder.getHandleState() != 2){
            map.put("code",5000);
            map.put("msg","订单为不可确认状态");
            return map;
        }else {
            expressOrder.setHandleState(3);
            ExpressOrder.ExpressInfo expressInfo = JSONObject.parseObject(expressOrder.getExpressInfo(), ExpressOrder.ExpressInfo.class);
            this.updateById(expressOrder);
            ExpressUser expressUser = expressUserService.selectOne(new EntityWrapper<ExpressUser>().eq("express_order_id", orderId).eq("complete_flag", 0));
            expressUser.setCompleteFlag(true);
            expressUserService.updateById(expressUser);
            UserWalletBO one = userWalletBORepository.findOne(expressUser.getUserId());
            one.setBalance(one.getBalance() + expressInfo.getPrice());
            one.setIntegral(one.getIntegral() + (int)Math.ceil(expressInfo.getPrice()/2));
            userWalletBORepository.save(one);
            map.put("code",0);
            map.put("msg","订单确认收货成功，钱款已打到对方账户");
            detailedLogService.insert(new DetailedLog(expressUser.getUserId(),1,"确认订单：余额 +"+expressInfo.getPrice()+"元"));
            detailedLogService.insert(new DetailedLog(expressUser.getUserId(),2,"确认订单：积分 +"+((int)Math.ceil(expressInfo.getPrice()/2)+"分")));
            return map;
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

    private ExpressListVO assembleExpressInfo(String position, ExpressOrder li, Boolean isGetDetail,ExpressUser expressUser) {
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
                liExpressInfo.put("expressMsg", expressInfo.getExpressMsg());
                liExpressInfo.put("code", expressInfo.getCode());
                expressListVO.setCommentInfo(expressCommentService.selectOne(new EntityWrapper<ExpressComment>().eq("express_order_id",li.getId())));
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
            expressListVO.setCreateTime(li.getCreateTime());
            expressListVO.setCreateAvatar(userInfoService.selectById(li.getCreateUser()).getAvatar());
            if(expressUser != null){
                expressListVO.setCommentInfo(expressCommentService.selectOne(new EntityWrapper<ExpressComment>().eq("express_order_id",li.getId())));
                if(expressUser.getCompleteFlag()){
                    distanceInfo = new HashMap<>(1);
                    distanceInfo.put("tips1","订单已完成");
                    expressListVO.setDistanceInfo(distanceInfo);
                }
            }
            return expressListVO;
        }
        return null;
    }

    private String getHandleStateInfo(Integer integer){
        switch (integer){
            case 0: return "未付款";
            case 1: return "已付款";
            case 2: return "已接单";
            case 3: return "已完成";
            case 4: return "已评价";
            default: return "未知";
        }
    }

}
