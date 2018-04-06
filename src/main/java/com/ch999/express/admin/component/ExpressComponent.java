package com.ch999.express.admin.component;

import com.ch999.express.admin.service.AddressService;
import com.ch999.express.admin.vo.ExpressVO;
import com.ch999.express.common.MapTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author hahalala
 */
@Component
@Slf4j
public class ExpressComponent {

    private static final String NEFU_POSITION = "45.72074,126.63818";

    @Resource
    private HandleStaticJson handleStaticJson;

    @Resource
    private AddressService addressService;

    /**
     * 根据返回的地址id来获取快递点列表，根据是否在林大划分
     *
     * @param addressId
     * @return
     */
    public List getExpressList(Integer addressId) {
        Map<String, Object> addressById = addressService.getAddressById(addressId);
        Object addressInfo = addressById.get("addressInfo");
        if (addressInfo != null) {
            Map addressInfo1 = (Map) addressInfo;
            Integer dictance = MapTools.getDistanceByPosition(NEFU_POSITION, addressInfo1.get("position").toString());
            if (dictance != null && dictance <= 2000 && dictance != -1) {
                try {
                    List<ExpressVO> nefuExpressPoint = handleStaticJson.getNefuExpressPoint();
                    return nefuExpressPoint;
                } catch (IOException e) {
                    log.error("Json解析失败，获取东林快递点失败");
                }
            }
            return handleStaticJson.getSimpleExpressPoint();
        }
        return handleStaticJson.getSimpleExpressPoint();
    }
}
