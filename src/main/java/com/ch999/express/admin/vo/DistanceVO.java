package com.ch999.express.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class DistanceVO {

    private Integer status;

    private Test result;

    @Data
    private static class Test{
        List<Elements> elements;
    }

    @Data
    private static class Elements{

        private Map<String,Object> from;

        private Map<String,Object> to;

        private Integer distance;

        private Integer duration;
    }

    public Integer getDistance(){
        return this.result.getElements().get(0).distance;
    }
}
