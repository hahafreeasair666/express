package com.ch999.express.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ch999.express.admin.entity.Address;
import com.ch999.express.admin.mapper.AddressMapper;
import com.ch999.express.admin.service.AddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.vo.AddUpdateAddressVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public Boolean addOrUpdateAddress(Integer userId, AddUpdateAddressVO addUpdateAddressVO) {
        //是否是新增
        if(addUpdateAddressVO.getId() == null){
            Address.AddressInfoVO addressInfoVO = new Address.AddressInfoVO(addUpdateAddressVO.getAddressInfo(),addUpdateAddressVO.getName(),addUpdateAddressVO.getMobile(),addUpdateAddressVO.getPosition());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("addressInfo",addressInfoVO);
            Address address = new Address(userId,jsonObject.toJSONString());
            return this.insert(address);
        }else {
            Address address = this.selectById(addUpdateAddressVO.getId());
            Address.AddressInfoVO addressInfoVO = new Address.AddressInfoVO(addUpdateAddressVO.getAddressInfo(),addUpdateAddressVO.getName(),addUpdateAddressVO.getMobile(),addUpdateAddressVO.getPosition());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("addressInfo",addressInfoVO);
            address.setAddressInfo(jsonObject.toJSONString());
            return this.updateById(address);
        }
    }

    @Override
    public List<Map<String,Object>> getAddressListByUserId(Integer userId) {
        List<Map<String,Object>> list = new ArrayList<>();
        this.selectList(new EntityWrapper<Address>().eq("user_id",userId)).forEach(li->{
            Map<String,Object> map = new HashMap<>();
            map.put("id",li.getId());
            map.put("userId",li.getUserId());
            Map map1 = JSONObject.parseObject(li.getAddressInfo(), Map.class);
            map.put("addressInfo",map1.get("addressInfo"));
            list.add(map);
        });
        return list;
    }

    @Override
    public Map<String, Object> getAddressById(Integer id) {
        Map<String,Object> map = new HashMap<>();
        Address address = this.selectById(id);
        if(address != null){
            map.put("id",id);
            map.put("userId",address.getUserId());
            map.put("addressInfo",JSONObject.parseObject(address.getAddressInfo(), Map.class).get("addressInfo"));
        }
        return map;
    }

    @Override
    public Boolean deleteAddressById(Integer userId, Integer id) {
        Address address = this.selectOne(new EntityWrapper<Address>().eq("user_id", userId).eq("id", id));
        if(address == null){
            return false;
        }
        return this.deleteById(id);
    }
}
