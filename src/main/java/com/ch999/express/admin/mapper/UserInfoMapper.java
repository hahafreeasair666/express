package com.ch999.express.admin.mapper;

import com.ch999.express.admin.entity.UserInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ch999.express.admin.vo.CenterInfoMapperVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 个人中心
     * @param userId
     * @return
     */
    CenterInfoMapperVO selectCenterInfo(@Param("userId") Integer userId);
}
