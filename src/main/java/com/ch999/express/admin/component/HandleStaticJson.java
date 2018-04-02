package com.ch999.express.admin.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ch999.express.admin.vo.ExpressVO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author hahalala
 */
@Component
public class HandleStaticJson {


    /**
     * 解析nefu静态快递点
     * @return
     * @throws IOException
     */
    public List<ExpressVO> getNefuExpressPoint() throws IOException{
        Resource resource = new ClassPathResource("static/nefuPoint.json");
        InputStream inputStream = resource.getInputStream();
        byte[] filecontent = new byte[(int)resource.getFile().length()];
        inputStream.read(filecontent);
        inputStream.close();
        Map map = JSONObject.parseObject(new String(filecontent, "utf-8"), Map.class);
        List<ExpressVO> data = JSONArray.parseArray(map.get("data").toString(), ExpressVO.class);
        return data;
    }


}
