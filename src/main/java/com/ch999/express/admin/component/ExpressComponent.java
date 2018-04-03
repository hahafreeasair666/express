package com.ch999.express.admin.component;

import com.ch999.express.admin.service.AddressService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hahalala
 */
@Component
public class ExpressComponent {

    @Resource
    private HandleStaticJson handleStaticJson;

    @Resource
    private AddressService addressService;

    /**
     * 根据返回的地址id来获取快递点列表，根据是否在林大划分
     * @param addressId
     * @return
     */
    public List<Object> getExpressList(Integer addressId){
        return null;
    }
}
