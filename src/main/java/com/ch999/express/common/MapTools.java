package com.ch999.express.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ch999.common.util.utils.HttpClientUtils;
import com.ch999.express.admin.vo.DistanceVO;
import com.ch999.express.admin.vo.ExpressVO;
import com.ch999.express.admin.vo.PositionByAddressVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hahalala
 */
@Slf4j
public class MapTools {

    /**
     * 人平均走路速度95m/s
     */
    private static final Integer AVGWALKSPEED = 95;

    private static final String TENCENT_KEY = "IYRBZ-SI6WO-ERXWT-S7BSF-LB4F6-2WBAS";

    private static final String GET_INFO_BY_KEYWORD = "http://apis.map.qq.com/ws/place/v1/suggestion?" + "key=" + TENCENT_KEY + "&keyword=%s";

    private static final String GET_DISTANCE = "http://apis.map.qq.com/ws/distance/v1/?" + "key=" + TENCENT_KEY + "&mode=walking&from=%s&to=%s";

    private static final String GET_POSITION_BY_ADDRESS = "http://apis.map.qq.com/ws/geocoder/v1/?address=%s&" + "key=" + TENCENT_KEY;

    /**
     * 根据关键字搜索地点
     *
     * @param keyWord
     * @return
     */
    public static List<ExpressVO> getAddressByKeyWord(String keyWord) {
        String format = String.format(GET_INFO_BY_KEYWORD, keyWord);
        String s = HttpClientUtils.get(format);
        Map map = JSONObject.parseObject(s, Map.class);
        try {
            if ((int) map.get("status") == 0) {
                return JSONArray.parseArray(map.get("data").toString(), ExpressVO.class);
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("json转化异常，地址获取失败");
            return new ArrayList<>();
        }
    }

    /**
     * 两坐标之间计算距离
     *
     * @param from
     * @param to
     * @return
     */
    public static Integer getDistanceByPosition(String from, String to) {
        String format = String.format(GET_DISTANCE, from, to);
        String s = HttpClientUtils.get(format);
        DistanceVO distanceVO = JSONObject.parseObject(s, DistanceVO.class);
        try {
            if (distanceVO.getStatus() == 0) {
                return distanceVO.getDistance();
            }
            return -1;
        } catch (Exception e) {
            log.error("地图调用失败，距离计算失败");
            return -1;
        }
    }

    /**
     * 通过（详细）地址获取坐标
     *
     * @param address
     * @return
     */
    public static ExpressVO getPositionByAddress(String address) {
        String format = String.format(GET_POSITION_BY_ADDRESS, address);
        String s = HttpClientUtils.get(format);
        PositionByAddressVO positionByAddressVO = JSONObject.parseObject(s, PositionByAddressVO.class);
        try {
            if (positionByAddressVO.getStatus() == 0) {
                return new ExpressVO("",positionByAddressVO.getTitle(),positionByAddressVO.getAddress(),positionByAddressVO.getLocation());
            }
            return new ExpressVO();
        } catch (Exception e) {
            log.error("json转化异常，地址获取失败");
            return new ExpressVO();
        }
    }
}
