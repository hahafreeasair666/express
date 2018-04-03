package com.ch999.express.admin.controller;


import com.ch999.common.util.vo.Result;
import com.ch999.express.admin.component.HandleStaticJson;
import com.ch999.express.common.MapTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@RestController
@RequestMapping("/admin/address")
public class AddressController {

    @Resource
    private HandleStaticJson handleStaticJson;

    @GetMapping("/test")
    public Result test(String str) {
        return Result.success(MapTools.getPositionByAddress(str));
    }

}

