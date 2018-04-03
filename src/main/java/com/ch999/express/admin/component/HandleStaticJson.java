package com.ch999.express.admin.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ch999.express.admin.vo.ExpressVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hahalala
 */
@Component
public class  HandleStaticJson {


    private static List<Map<String,Object>> simpleExpressPoint = null;

    /**
     * 解析nefu静态快递点,缓存时长没起作用不知道为啥，只能缓存一分钟
     * @return
     * @throws IOException
     */
    @Cacheable(cacheNames="nefuPoint",cacheManager = "oneDayCacheManager")
    public List<ExpressVO> getNefuExpressPoint() throws IOException{
        Resource resource = new ClassPathResource("static/neFuPoint.json");
        InputStream inputStream = resource.getInputStream();
        byte[] filecontent = new byte[(int)resource.getFile().length()];
        inputStream.read(filecontent);
        inputStream.close();
        Map map = JSONObject.parseObject(new String(filecontent, "utf-8"), Map.class);
        List<ExpressVO> data = JSONArray.parseArray(map.get("data").toString(), ExpressVO.class);
        return data;
    }

    public List<Map<String,Object>> getSimpleExpressPoint(){
        if(!CollectionUtils.isEmpty(simpleExpressPoint)){
            return simpleExpressPoint;
        }
        List<Map<String,Object>> list = new ArrayList<>();
        Stream.of("顺丰速运","EMS（中国邮政）","申通快递","中通快递","圆通快递","天天快递","百世汇通","韵达快递","其他快递").forEach(li->{
            Map<String,Object> map = new HashMap<>();
            map.put("id",list.size()+1);
            map.put("name",li);
            list.add(map);
        });
        simpleExpressPoint = list;
        return simpleExpressPoint;
    }
}
