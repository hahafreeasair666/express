package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.Address;
import com.baomidou.mybatisplus.service.IService;
import com.ch999.express.admin.vo.AddUpdateAddressVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface AddressService extends IService<Address> {

    /**
     * 新增修改用户收货地址
     * @param userId
     * @param addUpdateAddressVO
     * @return
     */
    Boolean addOrUpdateAddress(Integer userId,AddUpdateAddressVO addUpdateAddressVO);

    /**
     * 根据用户id获取地址列表
     * @param userId
     * @return
     */
    List<Map<String,Object>> getAddressListByUserId(Integer userId);

    /**
     * 根据地址id获取地址详情
     * @param id
     * @return
     */
    Map<String,Object> getAddressById(Integer id);

    /**
     * 根据id删除地址
     * @param userId
     * @param id
     * @return
     */
    Boolean deleteAddressById(Integer userId,Integer id);
}
